package com.marine.customerservice.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @JsonProperty("email")
    private String email = null;
    @JsonProperty("password")
    private String password = null;
    @JsonProperty("userName")
    private String userName = null;
}
