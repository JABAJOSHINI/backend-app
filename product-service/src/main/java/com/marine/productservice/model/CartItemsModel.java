package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsModel {
    @JsonProperty("productName")
    private String productName = null;
    @JsonProperty("price")
    private String price = null;
    @JsonProperty("image")
    private String image = null;

}

