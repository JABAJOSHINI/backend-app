package com.marine.productservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("country")
    private String country;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("message")
    private String message;
    @JsonProperty("company_address")
    private String companyName;
    @Column(name = "company_address")
    private String companyAddress;
}
