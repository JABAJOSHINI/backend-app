package com.marine.supplierservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "supplier_quote")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierQuoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="quote_id")
    private String quoteId = null;
    @Column(name ="currency")
    private String currency = null;
    @Column(name ="status")
    private String status = null;
    @Column(name ="created_at")
    private LocalDate createdAt = null;
    @Column(name ="valid_until")
    private LocalDate validUntil = null;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_contact_id")
    private SupplierDetailsEntity supplierDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_quote_item_id", nullable = false)
    private SupplierQuoteItemEntity supplierQuoteItem;

}
