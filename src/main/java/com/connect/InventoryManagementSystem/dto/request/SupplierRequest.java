package com.connect.InventoryManagementSystem.dto.request;

import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "Supplier Email is required")
    private String email;

    private SupplierStatus status;
}
