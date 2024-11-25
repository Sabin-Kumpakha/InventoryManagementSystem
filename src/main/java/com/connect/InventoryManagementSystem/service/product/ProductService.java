package com.connect.InventoryManagementSystem.service.product;

import com.connect.InventoryManagementSystem.dto.request.ProductRequest;
import com.connect.InventoryManagementSystem.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {

    ProductResponse getProductById(Long productId);

    List<ProductResponse> getAllProducts();

    ProductResponse createProduct(@Valid ProductRequest request);

    ProductResponse updateProduct(Long productId, @Valid ProductRequest request);

    void deleteProduct(Long productId);

    // import products from csv file
    int importProductsFromCsv(MultipartFile file);

    // export products to csv file
    byte[] exportToCsv();
}
