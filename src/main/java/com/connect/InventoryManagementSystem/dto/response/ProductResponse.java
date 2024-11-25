package com.connect.InventoryManagementSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long productId;
    private String productName;
    private String description;
    private double price;
    private int quantity;
    private String supplierName;
}
