package com.marine.customerservice.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.customerservice.Entity.LoginEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthModel {
    @JsonProperty("token")
    private String token = null;
    @JsonProperty("user")
    private LoginEntity user = null;
}
