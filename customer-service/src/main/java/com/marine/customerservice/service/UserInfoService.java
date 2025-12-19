package com.marine.customerservice.service;

import com.example.appcommons.utility.Response;
import com.marine.customerservice.Dto.LoginResponse;
import com.marine.customerservice.Entity.TokenEntity;
import com.marine.customerservice.Entity.UserInfoEntity;
import com.marine.customerservice.Exception.*;
import com.marine.customerservice.Model.AuthRequest;
import com.marine.customerservice.Model.ForgotPasswordModel;
import com.marine.customerservice.Model.UserInfoModel;
import com.marine.customerservice.Repository.TokenRepository;
import com.marine.customerservice.Repository.UserInfoRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepository userInforepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public UserInfoService(
            UserInfoRepository userInforepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            TokenRepository tokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userInforepository = userInforepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }
    Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    public UserInfoEntity authenticate(AuthRequest input) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getEmail(),
//                        input.getPassword()
//                )
//        );
//
//        UserInfoEntity user = userInforepository.findByEmail(input.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserInfoEntity user = userInforepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked. Please contact support.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUserName(), input.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userInforepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserInfoEntity  addUser(UserInfoModel userInfo) {
        logger.debug("User Added command received!!!");
        UserInfoEntity users = new UserInfoEntity();
        if(userInfo.getEmail() != null && userInfo.getPassword() != null && userInfo.getUserName() != null) {
            try {
                if (userInforepository.findByEmail(userInfo.getEmail()).isPresent()) {
                    throw new UserAlreadyExistException("User Already Exist.");
                }
                users.setUserName(userInfo.getUserName());
                users.setEmail(userInfo.getEmail());
                users.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                userInforepository.save(users);
            }catch (APIException e){
                throw new BadCredentialsException(e.getMessage());
            }
        }
        logger.debug("User Added successfully!!!");
        return users;
    }

    public UserInfoEntity signIn(AuthRequest request) {
        Optional<UserInfoEntity> users = null;
        if(request.getEmail() != null){
             users = userInforepository.findByEmail(request.getEmail());
        } else {
            throw new HandleGlobalException("Missing Email");
        }
        if (users.isEmpty() || !passwordEncoder.matches(request.getPassword(), users.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return users.get();
    }
    private static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofMinutes(60);

    // ...

//    public String generateRefreshToken(UserInfoEntity users ) {
//        final String newToken = UUID.randomUUID().toString();
//        TokenEntity refreshToken = new TokenEntity();
//        refreshToken.setToken(newToken);
//        refreshToken.setExpiresAt(OffsetDateTime.now().plus(REFRESH_TOKEN_VALIDITY).toInstant());
//        refreshToken.setUser(users);
//        tokenRepository.save(refreshToken);
//        return newToken;
//    }

    public String validateRefreshTokenAndGetUsername(final String givenToken) {
        final TokenEntity refreshToken = tokenRepository.findByTokenAndExpiresAt(givenToken, OffsetDateTime.now());
        if (refreshToken == null) {
           // log.warn("refresh token invalid");
           // throw new InvalidTokenException();
        }
       // return refreshToken.getUser().getId();
        return givenToken;
    }

    public UserDetails loadUserByEmail(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (Exception e) {
            throw new UserNotFoundException("User not found");
        }
        UserDetails user = loadUserByUsername(request.getEmail());
        return user;
    }
    public void checkValidUserOrThrow(UserDetails user, AuthRequest request) {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new PasswordNotMatchedException("Wrong Password.Login Failed");
        }
    }



    public Response logout(String refreshToken) {
        if(refreshToken == null) {
            throw new HandleGlobalException("Refresh Token Missing");
        }
        tokenRepository.deleteByToken(refreshToken);
        Response response = new Response();
        response.setMessage("SUCCESS");
        response.setStatus(Boolean.FALSE);
        return response;
    }

    public ResponseEntity<?> resetPassword(ForgotPasswordModel body) {
        try {
            Optional<UserInfoEntity> users = userInforepository.findByEmail(body.getEmail());
            if (users.isEmpty()) {
                throw new UserNotFoundException("user not found");
            }
            String newPassword = body.getNewPassword();
            String confirmPassword = body.getConformPassword();
            if (!newPassword.equals(confirmPassword)) {
                throw new PasswordNotMatchedException("Password not matched");
            }
            TokenEntity tokenEntity = tokenRepository.findByUserId(users.get().getId());
            if (tokenEntity == null || tokenEntity.getExpiresAt().isBefore(Instant.now())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
            UserInfoEntity user = tokenEntity.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userInforepository.save(user);
            tokenRepository.delete(tokenEntity);
            return ResponseEntity.ok("Password reset successful");
        }catch (APIException e) {
                logger.error("Password reset api error >>>>>>>>> "+e.getMessage());
                throw e;
            }

        }



    public LoginResponse getUserAuthentication(AuthRequest request, String accessToken, String refreshToken, UserDetails user) {
        LoginResponse loginResponse = new LoginResponse();
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUser((UserInfoEntity) user);
        tokenEntity.setExpiresAt(Instant.now().plusSeconds(604800)); // 7 days
        tokenRepository.save(tokenEntity);
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresAt(String.valueOf(tokenEntity.getExpiresAt()));
        logger.debug("User Login successfully!!!");
        return loginResponse;
    }
    @Transactional
    public ResponseEntity<?> signout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String accessToken = null;
            String refreshToken = null;
            // 🔥 Read cookies manually
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("accessToken")) {
                        accessToken = cookie.getValue();
                    }
                    if (cookie.getName().equals("refreshToken")) {
                        refreshToken = cookie.getValue();
                    }
                }
            }
            if (refreshToken != null) {
                tokenRepository.deleteByToken(refreshToken);
            }
            // ----- DELETE BOTH COOKIES -----
            deleteCookie("accessToken", response);
            deleteCookie("refreshToken", response);
//            ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "")
//                    .httpOnly(true)
//                    .secure(false) // same as login
//                    .sameSite("Lax")
//                    .path("/")
//                    .domain("localhost") // must match login
//                    .maxAge(0)           // DELETE COOKIE
//                    .build();

        //    response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        }catch(Exception e){
            logger.error("Logged out api command error >>>>>>>>> "+e.getMessage());
            throw e;
        }
        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully"
        ));
    }
    private void deleteCookie(String name, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(false)
                .domain("localhost") // SAME AS LOGIN
                .sameSite("Lax")    // SAME AS LOGIN
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
