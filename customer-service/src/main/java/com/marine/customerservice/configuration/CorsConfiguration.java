package com.marine.customerservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
////@EnableWebMvc
//    public class CorsConfiguration implements WebMvcConfigurer {
//
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**")
//                    .allowedOrigins("http://localhost:3000") // Allow frontend domain
//                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
//                    .allowedHeaders("*") // Allow all headers
//                    .allowCredentials(true); // Allow credentials (cookies, authorization headers)
//        }
//    }