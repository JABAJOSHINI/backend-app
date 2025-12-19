package com.marine.customerservice.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordModel {
    @JsonProperty("email")
    private String email = null;
    @JsonProperty("newPassword")
    private String newPassword = null;
    @JsonProperty("conformPassword")
    private String conformPassword = null;
}
