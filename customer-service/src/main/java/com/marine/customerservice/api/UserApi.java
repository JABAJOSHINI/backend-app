package com.marine.customerservice.api;

import com.example.appcommons.utility.Response;
import com.marine.customerservice.Dto.LoginResponse;
import com.marine.customerservice.Model.AuthRequest;
import com.marine.customerservice.Model.ForgotPasswordModel;
import com.marine.customerservice.Model.UserInfoModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RequestMapping("/api/auth")
public interface UserApi {
    @PostMapping(value = "/addNewUser", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Response> addNewUser(@RequestBody UserInfoModel userInfo);

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request, HttpServletResponse response);

    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    );

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) ;

    @PostMapping("/signout")
    ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response);

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordModel body);
}
