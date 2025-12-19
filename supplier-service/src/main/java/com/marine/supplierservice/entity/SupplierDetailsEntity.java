package com.marine.supplierservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="email")
    private String email = null;
    @Column(name ="phone")
    private String phone = null;
    @Column(name ="company")
    private String company = null;
}
