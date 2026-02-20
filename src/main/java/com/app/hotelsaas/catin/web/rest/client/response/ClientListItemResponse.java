package com.app.hotelsaas.catin.web.rest.client.response;

public record ClientListItemResponse(
        String id,
        String fullName,
        String document,
        String phone,
        String email,
        String status
) {}
