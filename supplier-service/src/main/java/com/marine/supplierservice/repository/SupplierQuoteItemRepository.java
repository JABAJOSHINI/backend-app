package com.marine.supplierservice.repository;

import com.marine.supplierservice.entity.SupplierQuoteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierQuoteItemRepository extends JpaRepository<SupplierQuoteItemEntity, Long> {
}
