package com.marine.productservice.repository;

import com.marine.productservice.DTO.ProductDto;
import com.marine.productservice.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
   // @Query("SELECT p FROM Products p WHERE p.category.categoryName = :category"
   Page<Products> findByCategory_CategoryName(String categoryName, Pageable pageable);
 @Query(value = "SELECT * FROM products " +
         "WHERE description ILIKE CONCAT('%', :value, '%') OR sku ILIKE CONCAT('%', :value, '%')",
         countQuery = "SELECT COUNT(*) FROM products WHERE description ILIKE CONCAT('%', :value, '%')OR sku ILIKE CONCAT('%', :value, '%')", nativeQuery = true)
 Page<Products> findByDescriptionOrSKUContaining(@Param("value") String value, Pageable pageable);


 // List<Products> findByCategory_CategoryName(String categoryName, Pageable pageable);
    @Query("SELECT p FROM Products p WHERE p.name = :productName")
    Products findOneByName(String productName);

    @Query("SELECT p FROM Products p WHERE p.name = :name and p.partNumber = :partNumber")
    Products findByNameAndPartNumber(String name, String partNumber);
    @Query("SELECT p FROM Products p WHERE p.name = :name")
    List<Products>  findByName(String name);
    @Query("SELECT p FROM Products p WHERE p.partNumber = :partNumber and p.description = :description")
    List<Products>  findByPartNumberAndDescription(String partNumber, String description);
    @Query("SELECT p FROM Products p WHERE p.description LIKE %?1%")
    List<Products>  findByDescription(String description);

    Products findBySku(String sku);
    @Query(value = "SELECT * FROM products WHERE description ILIKE CONCAT('%', :value, '%') OR sku ILIKE CONCAT('%', :value, '%')", nativeQuery = true)
    List<Products> findByDescriptionOrSKU(@Param("value") String value);
    @Query("SELECT p FROM Products p WHERE p.id = :id")
    Products findOneById(Long id);
    @Query("SELECT p FROM Products p WHERE p.name IN :names")
    List<Products> findByNames(@Param("names") List<String> names);

}
