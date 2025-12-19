package com.marine.customerservice.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marine.customerservice.Entity.LoginEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoModel {
    @JsonProperty("email")
    private String email = null;
    @JsonProperty("password")
    private String password = null;
    @JsonProperty("userName")
    private String userName = null;
    @Column(name = "roles")
    private String roles = null;
}
