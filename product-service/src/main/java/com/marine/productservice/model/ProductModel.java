package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image")
    private String image;
    @JsonProperty("description")
    private String description;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("price")
    private String price;
    @JsonProperty("equipment")
    private String equipment;
    @JsonProperty("make")
    private String make;
    @JsonProperty("model")
    private String model;
    @JsonProperty("partNumber")
    private String partNumber;
    @JsonProperty("specification")
    private String specification;
    @JsonProperty("created_at")
    private LocalDate createdAt;
    @JsonProperty("modified_at")
    private LocalDate modifiedAt;
    @JsonProperty("coo")
    private String coo;
    @JsonProperty("hazodus")
    private String hazodus;
    @JsonProperty("category")
    private String category;
    @JsonProperty("quantity")
    private Integer quantity;


}
