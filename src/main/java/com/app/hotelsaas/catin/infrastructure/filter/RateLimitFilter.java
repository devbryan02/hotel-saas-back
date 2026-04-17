package com.app.hotelsaas.catin.infrastructure.filter;

import com.app.hotelsaas.catin.infrastructure.security.CustomUserDetails;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
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
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<UUID, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createBucket(){
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(100)
                        .refillGreedy(100, Duration.ofMinutes(1))
                        .build())
                .build();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.getPrincipal() instanceof CustomUserDetails user){

            UUID tenantId = user.getTenantId();
            Bucket bucket = cache.computeIfAbsent(tenantId, k -> createBucket());

            if (bucket.tryConsume(1)) {
                log.info("Tenant {} has {} requests left", tenantId, bucket.getAvailableTokens());
                filterChain.doFilter(request, response);
            } else {

                log.warn("RATE LIMIT EXCEEDED for tenant {}", tenantId);
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Too many requests\"}");
                response.getWriter().flush();
                return;
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
