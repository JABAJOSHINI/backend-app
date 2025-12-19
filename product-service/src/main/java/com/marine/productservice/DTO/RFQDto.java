package com.marine.productservice.DTO;

import com.marine.productservice.entity.Products;
import com.marine.productservice.model.ContactModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RFQDto implements Serializable {
        private Long id ;
        private String rfqorderId;
        private ContactsDTO contacts ;
        private List<ProductDto> products;
        private LocalDate createdAt ;
        private LocalDate modifiedAt ;
}
