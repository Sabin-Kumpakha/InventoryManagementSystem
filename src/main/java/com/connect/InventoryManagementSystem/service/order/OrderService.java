package com.connect.InventoryManagementSystem.service.order;

import com.connect.InventoryManagementSystem.dto.request.OrderRequest;
import com.connect.InventoryManagementSystem.dto.response.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long orderId);

    OrderResponse createOrder(@Valid OrderRequest request);

    OrderResponse updateOrder(Long orderId, @Valid OrderRequest request);

    void deleteOrder(Long orderId);

    // import orders from csv
    int importOrdersFromCsv(MultipartFile file);

    // export orders to csv
    byte[] exportToCsv();

}
