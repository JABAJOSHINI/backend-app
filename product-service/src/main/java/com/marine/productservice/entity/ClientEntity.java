package com.marine.productservice.entity;

import com.marine.productservice.model.CartItemsModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client_contact_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name ="email")
    private String email;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "country")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Column(name = "dial_code")
    private String dialCode ;
    @Column(name = "company_address")
    private String companyAddress;
    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL)
    private Set<CartEntity> cartEntity = new HashSet<CartEntity>(0);

}
