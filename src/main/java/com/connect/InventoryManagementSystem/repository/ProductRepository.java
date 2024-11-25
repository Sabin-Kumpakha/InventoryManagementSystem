package com.connect.InventoryManagementSystem.repository;

import com.connect.InventoryManagementSystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
