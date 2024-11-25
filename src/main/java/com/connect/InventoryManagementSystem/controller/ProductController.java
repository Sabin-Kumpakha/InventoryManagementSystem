package com.connect.InventoryManagementSystem.controller;

import com.connect.InventoryManagementSystem.dto.request.ProductRequest;
import com.connect.InventoryManagementSystem.service.product.ProductService;
import com.connect.InventoryManagementSystem.utils.CSVUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = productService.getAllProducts();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Products retrieved successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProductById(Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = productService.getProductById(productId);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Product retrieved successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody ProductRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = productService.createProduct(request);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Product created successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            var data = productService.updateProduct(productId, request);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Product updated successfully");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.deleteProduct(productId);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Product deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importProductsFromCsv(@RequestParam("productCsv") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            int count = productService.importProductsFromCsv(file);
            response.put("status", HttpStatus.OK.value());
            response.put("message", count + " products imported successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProductsToCsv() {
        String filename = "ProductsExport.csv";
        return CSVUtils.getResponseEntity(productService.exportToCsv(), filename);
    }

}
