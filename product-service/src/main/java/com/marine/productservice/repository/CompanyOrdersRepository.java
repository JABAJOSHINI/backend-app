package com.marine.productservice.repository;

import com.marine.productservice.entity.CompanyOrdersEntity;
import com.marine.productservice.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyOrdersRepository extends JpaRepository<CompanyOrdersEntity, Long> {
}
