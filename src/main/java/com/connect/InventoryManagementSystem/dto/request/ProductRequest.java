package com.connect.InventoryManagementSystem.dto.request;

import com.connect.InventoryManagementSystem.model.Supplier;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Product price is required")
    @Min(value = 1, message = "Product price must be greater than 1")
    private Double price;

    @NotNull(message = "Product quantity is required")
    @Min(value = 1, message = "Product quantity must be greater than or equal to 1")
    private Integer quantity;

    @NotNull(message = "Product supplier is required")
    private Long supplierId;
}
