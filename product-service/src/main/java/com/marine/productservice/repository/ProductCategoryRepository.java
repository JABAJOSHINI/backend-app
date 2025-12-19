package com.marine.productservice.repository;

import com.marine.productservice.entity.ProductCategory;
import com.marine.productservice.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    ProductCategory findBycategoryName(String categoryName);
    //   Page<ProductCategory> findByName(String category, Pageable pageable);

}
