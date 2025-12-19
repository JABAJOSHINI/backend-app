package com.marine.customerservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email = null;
    @Column(name = "password")
    private String password = null;
    @Column(name = "user_name")
    private String userName = null;
}