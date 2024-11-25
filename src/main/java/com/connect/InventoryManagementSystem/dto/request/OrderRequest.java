package com.connect.InventoryManagementSystem.dto.request;

import com.connect.InventoryManagementSystem.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<OrderDetailRequest> orderDetails;

    @Data
    @Builder
    public static class OrderDetailRequest {
        private Long productId;

        @Min(value = 1, message = "Quantity must be greater than 0")
        private int quantity;
    }
}
