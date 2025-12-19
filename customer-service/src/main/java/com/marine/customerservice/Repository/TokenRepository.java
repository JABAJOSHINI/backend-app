package com.marine.customerservice.Repository;

import com.marine.customerservice.Entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    TokenEntity findByTokenAndExpiresAt(String givenToken, OffsetDateTime now);

    TokenEntity findByToken(String refreshToken);
    void deleteByToken(String refreshToken);

    TokenEntity findByUserId(Long id);


}
