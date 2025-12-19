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
public class CountryDto implements Serializable {
    private String name;
    private String dialCode;
}
