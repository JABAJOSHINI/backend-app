package com.marine.productservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PurchaseQuoteDto {
    private Long id = null;
    private String name = null;
    private String description = null;
    private String partNumber = null;
    private LocalDate createdAt = null;
    private int quantity;
    private String uom = null;
    private double totalPrice;
    private double unitPrice;
    private String remarks = null;
}
