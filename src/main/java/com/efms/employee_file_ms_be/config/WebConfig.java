package com.efms.employee_file_ms_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author Josue Veliz
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${efms.tenant.excluded-paths}")
    private String[] excludedPaths;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor(Arrays.asList(excludedPaths)));
    }
}
