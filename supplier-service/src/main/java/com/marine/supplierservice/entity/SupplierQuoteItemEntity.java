package com.marine.supplierservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_quoted_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierQuoteItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="product_id")
    private Long productId = null;
    @Column(name ="quantity")
    private Integer quantity = null;
    @Column(name ="unit_price")
    private Double unitPrice = null;
    @Column(name ="total_price")
    private Double totalPrice = null;
}
