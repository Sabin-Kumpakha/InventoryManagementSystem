package com.connect.InventoryManagementSystem.service.order;

import com.connect.InventoryManagementSystem.dto.request.OrderRequest;
import com.connect.InventoryManagementSystem.dto.response.OrderResponse;
import com.connect.InventoryManagementSystem.enums.OrderStatus;
import com.connect.InventoryManagementSystem.exception.NotFoundException;
import com.connect.InventoryManagementSystem.model.Order;
import com.connect.InventoryManagementSystem.model.OrderDetail;
import com.connect.InventoryManagementSystem.model.Product;
import com.connect.InventoryManagementSystem.repository.OrderRepository;
import com.connect.InventoryManagementSystem.repository.ProductRepository;
import com.connect.InventoryManagementSystem.repository.SupplierRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::orderResponseBuilder)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::orderResponseBuilder)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public OrderResponse createOrder(@Valid OrderRequest request) {
        Order order = orderRepository.save(convertToOrder(request));
        return orderResponseBuilder(order);
    }


    @Override
    public OrderResponse updateOrder(Long orderId, @Valid OrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(request.getOrderStatus());
        order.setOrderDate(LocalDate.now());

        Map<Long, Integer> existingItems = new HashMap<>();
        List<Long> removedItems = new ArrayList<>();
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            existingItems.put(orderDetail.getProduct().getProductId(), orderDetail.getQuantity());
            removedItems.add(orderDetail.getId());
        }

        order.getOrderDetails().clear();
        double totalAmount = 0;
        order.setTotalAmount(totalAmount);

        for (OrderRequest.OrderDetailRequest newOrderDetail : request.getOrderDetails()) {
            if (newOrderDetail.getQuantity() == 0)
                continue;
            Product product = productRepository.findById(newOrderDetail.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + newOrderDetail.getProductId()));

            int newQuantity = newOrderDetail.getQuantity();
            int existingQuantity = existingItems.getOrDefault(product.getProductId(), 0);
            if (newQuantity > existingQuantity) {
                // if new quantity is greater than existing quantity, then we need to add the difference to the product quantity
                product.setQuantity(product.getQuantity() - (newQuantity - existingQuantity));
            } else {
                // if new quantity is less than existing quantity, then we need to subtract the difference from the product quantity
                product.setQuantity(product.getQuantity() + (existingQuantity - newQuantity));
            }
            productRepository.save(product);

            OrderDetail updatedOrderDetail = new OrderDetail(product, newQuantity, product.getPrice() * newQuantity);
            order.getOrderDetails().add(updatedOrderDetail);

            totalAmount += updatedOrderDetail.getPrice();
            order.setTotalAmount(order.getTotalAmount() + updatedOrderDetail.getPrice());
        }
        orderRepository.save(order);
        removedItems.forEach(orderRepository::deleteById);
        return orderResponseBuilder(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        orderRepository.deleteById(orderId);
    }

    public int importOrdersFromCsv(MultipartFile file) {
        Map<Long, Order> orderMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext(); // Skip header
            int count = 0;

            while ((nextLine = reader.readNext()) != null) {
                Long orderId = Long.parseLong(nextLine[0]);
                LocalDate orderDate = LocalDate.parse(nextLine[1], DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                OrderStatus status = OrderStatus.valueOf(nextLine[3]);
                Long productId = Long.parseLong(nextLine[4]);
                double price = Double.parseDouble(nextLine[2]);
                int quantity = Integer.parseInt(nextLine[5]);

                Order order = orderMap.computeIfAbsent(
                        orderId,
                        id -> Order.builder()
                                .orderId(id)
                                .orderDate(orderDate)
                                .orderStatus(status)
                                .totalAmount(0)
                                .orderDetails(new ArrayList<>())
                                .build()
                );

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

                // Check for product quantity and update it
                if (product.getQuantity() < quantity) {
                    throw new NotFoundException("Product " + product.getProductName() + " is out of stock");
                }

                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);

                OrderDetail orderDetail = new OrderDetail(product, quantity, price);
                order.getOrderDetails().add(orderDetail);

                order.setTotalAmount(order.getTotalAmount() + price * quantity);
                count++;
            }
            orderRepository.saveAll(orderMap.values());
            return count;
        } catch (Exception e) {
            throw new NotFoundException("Failed to import orders from CSV file: " + e.getMessage());
        }
    }


    @Override
    public byte[] exportToCsv() {
        List<Order> orders = orderRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try(CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            writer.writeNext(new String[]{String.format("As of %s: ", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))});
            writer.writeNext(new String[]{"Order ID", "Order Date", "Total Amount", "Order Status", "Product ID", "Price", "Quantity"});

            for (Order order : orders) {
                int count = 0;
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    writer.writeNext(new String[]{
                            order.getOrderId().toString(),
                            order.getOrderDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                            String.valueOf(order.getTotalAmount()),
                            order.getOrderStatus().toString(),
                            orderDetail.getProduct().getProductId().toString(),
                            String.valueOf(orderDetail.getPrice()),
                            String.valueOf(orderDetail.getQuantity())
                    });
                    count++;
                }
            }
            writer.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new NotFoundException("CSV could not be exported " + e.getMessage());
        }
    }

    private OrderResponse orderResponseBuilder(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .products(order.getOrderDetails().stream()
                        .map(orderItem -> OrderResponse.ProductDetailResponse.builder()
                                .productId(orderItem.getProduct().getProductId())
                                .productName(orderItem.getProduct().getProductName())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Order convertToOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderDate(LocalDate.now());

        if (request.getOrderStatus() == null) {
            order.setOrderStatus(OrderStatus.PENDING);
        } else {
            order.setOrderStatus(request.getOrderStatus());
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalAmount = 0;

        for (OrderRequest.OrderDetailRequest orderedDetail : request.getOrderDetails()) {
            Product product = productRepository.findById(orderedDetail.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + orderedDetail.getProductId()));

            if (product.getQuantity() < orderedDetail.getQuantity()) {
                throw new NotFoundException("Product " + product.getProductName() + " is out of stock");
            }

            double price = product.getPrice() * orderedDetail.getQuantity();
            totalAmount += price;

            OrderDetail orderDetail = new OrderDetail(product, orderedDetail.getQuantity(), price);
            orderDetails.add(orderDetail);

            product.setQuantity(product.getQuantity() - orderedDetail.getQuantity());
            productRepository.save(product);
        }
        order.setTotalAmount(totalAmount);
        order.setOrderDetails(orderDetails);
        return order;
    }
}
