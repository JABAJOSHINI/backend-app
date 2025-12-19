package com.marine.supplierservice.model;

import java.time.LocalDate;

public interface SupplierQuotes {
    String getSupplierQuote();
    String getCompany();
    String getSupplierEmail();
    LocalDate getCreatedAt();
    String getStatus();
}
