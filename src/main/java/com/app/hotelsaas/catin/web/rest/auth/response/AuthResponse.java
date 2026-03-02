package com.app.hotelsaas.catin.web.rest.auth.response;

public record AuthResponse(
        String token,
        String email,
        String role,
        String tenantId
) {}