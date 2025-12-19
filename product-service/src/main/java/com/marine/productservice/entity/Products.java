package com.marine.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "impa_code")
    private String impaCode;
    @Column(name = "filter")
    private String filter;
    @Column(name = "image")
    private String image;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "sku")
    private String sku;
    @Column(name = "price")
    private String price;
    @Column(name = "equipment")
    private String equipment;
    @Column(name = "make")
    private String make;
    @Column(name = "model")
    private String model;
    @Column(name = "part_number")
    private String partNumber;
    @Column(name = "specification")
    private String specification;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "modified_at")
    private LocalDate modifiedAt;
    @Column(name = "coo")
    private String coo;
    @Column(name = "hazodus")
    private String hazodus;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;
    @OneToOne
    @JoinColumn(name = "inventory_id", unique = true)
    private ProductInventory inventory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

}
