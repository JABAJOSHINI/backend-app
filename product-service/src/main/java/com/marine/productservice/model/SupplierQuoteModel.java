package com.marine.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.productservice.DTO.RFQDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierQuoteModel {
    @JsonProperty("supplierEmails")
    private List<String> emails;
    @JsonProperty("rfq")
    private RFQDto rfq;
    @JsonProperty("purchaseQuoteId")
    private String purchaseQuoteId;
}
