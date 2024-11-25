package com.connect.InventoryManagementSystem.repository;

import com.connect.InventoryManagementSystem.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
