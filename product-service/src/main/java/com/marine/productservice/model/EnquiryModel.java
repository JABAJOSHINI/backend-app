package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.productservice.DTO.CountryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryModel
{
    @JsonProperty("email")
    private String email;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("vessel")
    private String vessel;
    @JsonProperty("equipment")
    private String equipment;
    @JsonProperty("imo")
    private String imo;
    @JsonProperty("make")
    private String make;
    @JsonProperty("model")
    private String model;
    @JsonProperty("firstName")
    private String fullName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("country")
    private CountryDto country;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("companyAddress")
    private String companyAddress;
    @JsonProperty("message")
    private String message;
    @JsonProperty("rfq")
    private List<RFQModel> rfq;
    @JsonProperty("cart")
    private List<CartModel> cart;


}
