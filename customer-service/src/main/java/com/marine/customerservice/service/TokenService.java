package com.marine.customerservice.service;

import com.marine.customerservice.Entity.TokenEntity;
import com.marine.customerservice.Repository.TokenRepository;
import com.marine.customerservice.configuration.JwtService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
        private final TokenRepository tokenRepository;
        private final JwtService jwtService;

        public TokenService(TokenRepository tokenRepository, JwtService jwtService) {
            this.tokenRepository = tokenRepository;
            this.jwtService = jwtService;
        }

        public boolean isRefreshTokenValid(String refreshToken) {
            TokenEntity storedToken = tokenRepository.findByToken(refreshToken);
            if (storedToken != null) {
                TokenEntity tokenEntity = storedToken;
                // Check if the token is expired
                return !jwtService.isTokenExpired(refreshToken);
            }
            return false;
        }
}
