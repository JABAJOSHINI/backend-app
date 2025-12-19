package com.marine.supplierservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SupplierImportFileModel {
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
