package com.marine.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000"); // your Next.js frontend
        corsConfig.addAllowedMethod("*"); // allow all HTTP methods
        corsConfig.addAllowedHeader("*"); // allow all headers
        corsConfig.setAllowCredentials(true); // important if you use cookies or Authorization headers
        corsConfig.addExposedHeader("Set-Cookie"); // expose Set-Cookie header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
