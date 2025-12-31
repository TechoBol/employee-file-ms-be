package com.efms.employee_file_ms_be.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Josue Veliz
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${spring.web.cors.allowed-origins:http://localhost:5173}")
    private String[] allowedOrigins;

    @Value("${spring.web.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${spring.web.cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization", "X-Company-Id", "X-USER-NAME", "X-USER-ID")
                .exposedHeaders("X-Company-Id", "X-USER-NAME", "X-USER-ID")
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
}
