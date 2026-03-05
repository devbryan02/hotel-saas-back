package com.app.hotelsaas.catin.web.rest.client.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ClientListItemResponse(
        String id,
        String fullName,
        String document,
        String phone,
        String email,
        String status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime lastStayAt
) {}
