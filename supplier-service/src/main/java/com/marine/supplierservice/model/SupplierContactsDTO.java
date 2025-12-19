package com.marine.supplierservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SupplierContactsDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("email")
    private String email;
    @JsonProperty("company")
    private String company;
    @JsonProperty("phone")
    private String phone;
    @Column(name = "created_at")
    private String created_at;
}
