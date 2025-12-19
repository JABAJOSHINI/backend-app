package com.marine.productservice.repository;

import com.marine.productservice.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    ClientEntity findByEmail(String email);
}
