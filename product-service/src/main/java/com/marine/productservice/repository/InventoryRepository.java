package com.marine.productservice.repository;

import com.marine.productservice.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<ProductInventory, Long> {
}
