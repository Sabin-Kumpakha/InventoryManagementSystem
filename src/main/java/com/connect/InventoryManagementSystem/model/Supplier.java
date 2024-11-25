package com.connect.InventoryManagementSystem.model;
import com.connect.InventoryManagementSystem.enums.SupplierStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    private String supplierName;

    private String phoneNumber;

    private String email;

    private SupplierStatus status;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> productBySupplier;
}
