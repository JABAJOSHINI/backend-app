package com.marine.productservice.DTO;

import com.marine.productservice.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CartDto implements Serializable {

    Products products = null;
    Integer quantity = null;
}
