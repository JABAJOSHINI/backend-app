package com.marine.productservice.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CategoryDto implements Serializable {
    private Long id;

    private String categoryName;
    private String description;

    private String createdAt;

    private String modifiedAt;
}
