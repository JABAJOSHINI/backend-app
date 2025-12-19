//package com.marine.customerservice.service;
//
//import com.marine.customerservice.Entity.LoginEntity;
//import com.marine.customerservice.Entity.UserInfoEntity;
//import com.marine.customerservice.Repository.LoginRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class UserInfoDetails implements UserDetails {
//
//    private String username; // Changed from 'name' to 'email' for clarity
//    private String password;
//    private List<GrantedAuthority> authorities;
//
//    public UserInfoDetails(UserInfoEntity userInfo) {
//        this.username = userInfo.getEmail(); // Use email as username
//        this.password = userInfo.getPassword();
//
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
