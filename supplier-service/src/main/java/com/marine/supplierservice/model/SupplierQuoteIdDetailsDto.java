package com.marine.supplierservice.model;

import com.marine.productservice.DTO.ContactsDTO;
import com.marine.productservice.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SupplierQuoteIdDetailsDto {
    private Long id ;
    private String supplierQuoteId;
    private SupplierContactsDTO contacts ;
    private List<ProductDto> products;
    private LocalDate createdAt ;
    private LocalDate modifiedAt ;
}
