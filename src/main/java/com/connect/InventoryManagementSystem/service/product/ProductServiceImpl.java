package com.connect.InventoryManagementSystem.service.product;

import com.connect.InventoryManagementSystem.dto.request.ProductRequest;
import com.connect.InventoryManagementSystem.dto.response.ProductResponse;

import com.connect.InventoryManagementSystem.exception.CSVException;
import com.connect.InventoryManagementSystem.exception.NotFoundException;
import com.connect.InventoryManagementSystem.model.Product;
import com.connect.InventoryManagementSystem.model.Supplier;
import com.connect.InventoryManagementSystem.repository.ProductRepository;
import com.connect.InventoryManagementSystem.repository.SupplierRepository;
import com.connect.InventoryManagementSystem.service.supplier.SupplierService;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;

    @Override
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::productResponseBuilder)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::productResponseBuilder)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse createProduct( @Valid ProductRequest request) {
        //if supplier doesn't exist throw exception
        if (!supplierService.existsById(request.getSupplier().getSupplierId())) {
            throw new NotFoundException("Product failed to save. Supplier doesn't exist.");
        }
        Product product = productRepository.save(convertToProduct(request));
        return productResponseBuilder(product);
    }

    @Override
    public ProductResponse updateProduct(Long productId, @Valid ProductRequest request) {
        Product product = convertToProduct(request);
        return productRepository.findById(productId)
                .map(savedProduct -> {
                    savedProduct.setProductName(product.getProductName());
                    savedProduct.setDescription(product.getDescription());
                    savedProduct.setPrice(product.getPrice());
                    savedProduct.setQuantity(product.getQuantity());
                    savedProduct.setSupplier(product.getSupplier());
                    productRepository.save(savedProduct);
                    return productResponseBuilder(savedProduct);
                })
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    @Override
    public void deleteProduct(Long productId) {
        // if product doesn't exist throw exception
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    public int importProductsFromCsv(MultipartFile file) {
        try(CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            reader.readNext();
            int count = 0;

            while ((nextLine = reader.readNext()) != null) {
                Product product = new Product();
                product.setProductName(nextLine[0]);
                product.setDescription(nextLine[1]);
                product.setPrice(Double.parseDouble(nextLine[2]));
                product.setQuantity(Integer.parseInt(nextLine[3]));

                Long supplierId = Long.parseLong(nextLine[4]);
                Supplier supplier = supplierRepository.findById(supplierId)
                        .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
                product.setSupplier(supplier);

                productRepository.save(product);
                count++;
            }
            reader.close();
            return count;
        } catch (Exception e) {
            throw new CSVException("Failed to import products from csv file" + e.getMessage());
        }
    }

    @Override
    public byte[] exportToCsv() {
        List<Product> listOfProducts = productRepository.findAll();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            String[] header = {"ID", "Product Name", "Description", "Price", "Quantity", "Supplier Id"};
            writer.writeNext(header);

            for (Product product : listOfProducts) {
                String[] line = {
                        String.valueOf(product.getProductId()),
                        product.getProductName(),
                        product.getDescription(),
                        String.valueOf(product.getPrice()),
                        String.valueOf(product.getQuantity()),
                        String.valueOf(product.getSupplier().getSupplierId())
                };
                writer.writeNext(line);
            }
            writer.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new CSVException("Failed to export products to csv file" + e.getMessage());
        }
    }

    public ProductResponse productResponseBuilder(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .supplierName(supplierService.getSupplierById(product.getSupplier().getSupplierId()))
                .build();
    }

    private Product convertToProduct(@Valid ProductRequest request) {
        return Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .supplier(request.getSupplier())
                .build();
    }
}
