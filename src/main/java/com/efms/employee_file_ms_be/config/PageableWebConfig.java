package com.efms.employee_file_ms_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Josue Veliz
 */
@Configuration
public class PageableWebConfig implements WebMvcConfigurer {

    /**
     * Configures how Spring resolves pagination parameters (Pageable) from HTTP requests.
     * - Sets a maximum page size to avoid excessive data loading.
     * - Sets pagination to be 0-indexed (page=0 is the first page).
     * - Provides a fallback Pageable if the client doesn't provide one.
     *
     * @param resolvers A list of argument resolvers to which the customized Pageable resolver is added.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setMaxPageSize(Constants.StaticNumbers.MAX_PAGE_SIZE);
        resolver.setOneIndexedParameters(false);
        resolver.setFallbackPageable(PageRequest.of(0, 20));
        resolvers.add(resolver);
    }
}
