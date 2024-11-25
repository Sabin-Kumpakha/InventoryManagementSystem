package com.connect.InventoryManagementSystem.dto.response;

import com.connect.InventoryManagementSystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private OrderStatus orderStatus;
    private double totalAmount;

    private List<ProductDetailResponse> products;

    @Data
    @Builder
    public static class ProductDetailResponse {
        private Long productId;
        private String productName;
        private double price;
        private int quantity;
    }
}
