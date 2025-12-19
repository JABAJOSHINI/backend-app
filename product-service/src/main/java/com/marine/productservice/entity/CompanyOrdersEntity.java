package com.marine.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
    @Entity
    @Table(name = "company_orders")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CompanyOrdersEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column(name ="order_number")
        private String orderNumber = null;
        @Column(name ="company_name")
        private String companyName = null;
}
