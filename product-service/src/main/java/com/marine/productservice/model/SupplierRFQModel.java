package com.marine.productservice.model;

import com.marine.productservice.DTO.ContactsDTO;
import com.marine.productservice.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRFQModel {
    private Long id ;
    private String RFQOrderId;
    private ContactsDTO contacts ;
    private List<ProductDto> products;
    private LocalDate createdAt ;
    private LocalDate modifiedAt ;
}
