package com.marine.productservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="product_name")
    private String productName = null;
    @Column(name ="price")
    private String price = null;
    @Column(name ="image")
    private String image = null;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity clientEntity;
}
