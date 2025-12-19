package com.marine.productservice.repository;

import com.marine.productservice.entity.CountryEntity;
import com.marine.productservice.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long>

    {
}
