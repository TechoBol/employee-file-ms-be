package com.efms.employee_file_ms_be.config;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @author Josue Veliz
 */
@AllArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {

    private static final String HEADER = "X-Company-Id";

    private List<String> excludedPaths;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String path = request.getRequestURI();
        if (isPathExcluded(path)) {
            return true;
        }

        String companyId = request.getHeader(HEADER);
        if (companyId == null || companyId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        TenantContext.setTenantId(companyId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        TenantContext.clear();
    }

    private boolean isPathExcluded(String path) {
        return excludedPaths.stream()
                .anyMatch(path::startsWith);
    }
}
