package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.productservice.DTO.CountryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactModel {
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
}
