package com.digital.digitalcommunicationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private String fromEmail;
    private String subject;
    private String companyName;
    private String vessel;
    private String imo;
    private String referenceNo;
    private String equipment;
    private String make;
    private LocalDateTime date;
    private String modelNumber;
    private String serialNumber;
    private String fullName;
    private String country;
    private String address;
    private String city;
    private String postalCode;
    private String email;
    private String phone;
    private String description;

    private String product;

}
