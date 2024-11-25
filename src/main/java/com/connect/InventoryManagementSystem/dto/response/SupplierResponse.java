package com.connect.InventoryManagementSystem.dto.response;

import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {

        private Long supplierId;

        private String supplierName;

        private String phoneNumber;

        private String email;

        private SupplierStatus status;
}
