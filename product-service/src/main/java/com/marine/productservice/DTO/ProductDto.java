package com.marine.productservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ProductDto implements Serializable {
    private Long id = null;
    private String name = null;
    private String description = null;
    private String sku = null;
    private String price = null;
    private LocalDate createdAt = null;
    private LocalDate modifiedAt = null;
    private String categoryName = null;
    private Set<CategoryDto> productCategory = null;
    private String image;
    private String equipment;
    private String make;
    private String model;
    private String partNumber;
    private String specification;
    private List<VariantDto> variants;
}
