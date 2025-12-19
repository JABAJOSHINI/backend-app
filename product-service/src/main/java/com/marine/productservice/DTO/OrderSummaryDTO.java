package com.marine.productservice.DTO;

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
public class OrderSummaryDTO implements Serializable {
    private String RFQOrderId;
    private Long clientID ;
    private String clientEmail ;
    private LocalDate createdAt ;
}
