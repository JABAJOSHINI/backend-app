package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RFQModel {
    @JsonProperty("description")
    private String description;
    @JsonProperty("part_number")
    private String partNumber;
    @JsonProperty("make")
    private String make;
    @JsonProperty("model")
    private String model;
    @JsonProperty("quantity")
    private Integer quantity;
}
