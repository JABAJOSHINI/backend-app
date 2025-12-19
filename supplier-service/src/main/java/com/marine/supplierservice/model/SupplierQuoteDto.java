package com.marine.supplierservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
    public class SupplierQuoteDto implements Serializable {
    private String supplierQuote;
    private String company;
    private String supplierEmail;
    private LocalDate createdAt;
    private String status;
    }



