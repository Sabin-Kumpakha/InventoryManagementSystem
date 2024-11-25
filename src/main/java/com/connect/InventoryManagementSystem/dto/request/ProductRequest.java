package com.connect.InventoryManagementSystem.dto.request;

import com.connect.InventoryManagementSystem.model.Supplier;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotBlank(message = "Product price is required")
    private double price;

    @NotBlank(message = "Product quantity is required")
    @Min(value = 0, message = "Product quantity must be greater than 0")
    private int quantity;

    @NotBlank(message = "Product supplier is required")
    private Supplier supplier;
}
