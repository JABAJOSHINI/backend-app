package com.marine.productservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "modified_at")
    private String modifiedAt;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Products> products = new HashSet<Products>(0);

}
