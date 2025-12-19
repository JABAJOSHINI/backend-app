
package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.productservice.DTO.CountryDto;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class checkoutModel {
    @JsonProperty("email")
    private String email;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("fullName")
    private CountryDto country;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("companyAddress")
    private String companyAddress;
    @JsonProperty("cart")
    private List<CartItemsModel> cart;
}
