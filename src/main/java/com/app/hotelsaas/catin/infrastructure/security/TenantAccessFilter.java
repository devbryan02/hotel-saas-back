package com.app.hotelsaas.catin.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class TenantAccessFilter extends OncePerRequestFilter {

    private static final Pattern TENANT_PATH = Pattern.compile(".*/tenants/([0-9a-fA-F-]{36})(?:/.*)?");

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        Matcher matcher = TENANT_PATH.matcher(uri);

        if (!matcher.matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID pathTenantId;
        try {
            pathTenantId = UUID.fromString(matcher.group(1));
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid tenantId\"}");
            response.getWriter().flush();
            return;
        }

        if (!pathTenantId.equals(userDetails.getTenantId())) {
            log.warn("Tenant mismatch. pathTenantId={}, tokenTenantId={}, uri={}", pathTenantId, userDetails.getTenantId(), uri);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"Tenant mismatch\"}");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
