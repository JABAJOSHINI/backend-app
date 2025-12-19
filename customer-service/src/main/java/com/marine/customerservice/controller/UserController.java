package com.marine.customerservice.controller;
import com.example.appcommons.utility.Response;
import com.marine.customerservice.Entity.TokenEntity;
import com.marine.customerservice.Entity.UserInfoEntity;
import com.marine.customerservice.Exception.APIException;
import com.marine.customerservice.Model.AuthRequest;
import com.marine.customerservice.Model.ForgotPasswordModel;
import com.marine.customerservice.Model.UserInfoModel;
import com.marine.customerservice.Repository.TokenRepository;
import com.marine.customerservice.Repository.UserInfoRepository;
import com.marine.customerservice.api.UserApi;
import com.marine.customerservice.configuration.JwtService;
import com.marine.customerservice.service.TokenService;
import com.marine.customerservice.service.UserInfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController implements UserApi {
    private final JwtService jwtService;

    private final UserInfoService userInfoService;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final UserInfoRepository userInforepository;
    public UserController(JwtService jwtService, UserInfoService userInfoService, TokenRepository tokenRepository, TokenService tokenService, UserInfoRepository userInforepository) {
        this.jwtService = jwtService;
        this.userInfoService = userInfoService;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.userInforepository = userInforepository;
    }
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public ResponseEntity<?> refreshToken(Map<String, String> request) {
        if (tokenService.isRefreshTokenValid(request.get("refreshToken"))) {
           // UserDetails userDetails = extractUserDetails(refreshToken);
            String newAccessToken = jwtService.generateRefreshToken(extractUserDetails(request.get("refreshToken")));
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.status(403).body("Refresh token expired or invalid");
        }
    }

    @Override
    public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response) {
       return userInfoService.signout(request, response);
    }

    @Override
    public ResponseEntity<?> resetPassword(ForgotPasswordModel body) {
        logger.debug("ResetPassword command received");
        return userInfoService.resetPassword(body);
    }

    public UserDetails extractUserDetails(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        // Load user details from database (or other source)
        return userInfoService.loadUserByUsername(username);
    }




    public ResponseEntity<?> authenticate(AuthRequest request, HttpServletResponse response) {
        logger.debug("Login command received!!!");
        try {
            UserDetails user = userInfoService.loadUserByEmail(request);
            userInfoService.checkValidUserOrThrow(user, request);
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
             userInfoService.getUserAuthentication(request, accessToken, refreshToken,user);
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
//                    .secure(true) // true if using HTTPS
//                    .sameSite("None") //prod
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 days
                    .secure(false)
                    .domain("localhost")
                    .sameSite("Lax") // safe default, works with localhost
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)        // important for security
                    .secure(false)         // true in production (HTTPS)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)  // 7 days
                    .domain("localhost")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            // 4. Return response
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "userName", user.getUsername(),
                    "email", request.getEmail()));
        }catch (APIException e) {
            logger.error("User Login error >>>>>>>>> "+e.getMessage());
            throw e;
        }
    }


    @Override
    public ResponseEntity<Response> addNewUser(@RequestBody UserInfoModel userInfo) {
        try {
            UserInfoEntity info = userInfoService.addUser(userInfo);
            Response resp = new Response();
            resp.setMessage("User added successfully!");
            resp.setStatus(true);
            resp.setData(info);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            Response resp = new Response();
            resp.setMessage(e.getMessage());
            resp.setStatus(false);
            resp.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(resp);
        }
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkLogin(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        if (accessToken != null && jwtService.validateToken(accessToken)) {
            return ResponseEntity.ok(Map.of("authenticated", true));
        }

        if (refreshToken != null && jwtService.validateToken(refreshToken)) {
            return ResponseEntity.ok(Map.of("authenticated", true));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("authenticated", false));
    }

//    public ResponseEntity<?> checkLogin(@CookieValue(value = "accessToken", required = false) String token) {
//        if (token == null || !jwtService.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized"));
//        }
//        String email = jwtService.extractUsername(token);
//        UserInfoEntity user = userInforepository.findByEmail(email).orElseThrow();
//        return ResponseEntity.ok(Map.of(
//                "userName", user.getUsername(),
//                "email", user.getEmail()
//        ));
//    }


}
