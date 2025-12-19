package com.marine.productservice.DTO;

import java.time.LocalDate;

public interface OrderSummary {
    String getOrderId();
    String getClientEmail();
    Long getClientId();
    LocalDate getCreatedAt();
}
