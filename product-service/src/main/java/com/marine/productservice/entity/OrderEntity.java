package com.marine.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rfq_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rfq_order_id")
    private String rfqOrderId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity clientEntity;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "modified_at")
    private LocalDate modifiedAt;
    @Column(name = "status")
    private String status;
    @Column(name = "quantity")
    private Integer quantity;

}
