package com.marine.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "product_inventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "modified_at")
    private LocalDate modifiedAt;

    @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL)
    private Products product;


}
