package com.olucaseduardo.zoomatech_api.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomCorsConfiguration implements CorsConfigurationSource {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        
        List<String> origins = (allowedOrigins != null && allowedOrigins.length > 0)
                ? Arrays.stream(allowedOrigins).map(String::trim).filter(s -> !s.isEmpty()).toList()
                : List.of("*");

        if (origins.isEmpty() || origins.contains("*")) {
            config.setAllowedOriginPatterns(List.of("*"));
        } else {
            config.setAllowedOriginPatterns(origins);
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        return config;
    }
}
