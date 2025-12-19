package com.marine.customerservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserInfoEntity user;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 1000)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @PrePersist
    public void setExpiration() {
        this.expiresAt = Instant.now().plus(7, ChronoUnit.DAYS); // 7 days validity
    }
}
