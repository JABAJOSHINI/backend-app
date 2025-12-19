package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartModel {
    @JsonProperty("name")
    private String name;
    @JsonProperty("make")
    private String make;
    @JsonProperty("price")
    private String price;
    @JsonProperty("quantity")
    private Integer quantity;
}
