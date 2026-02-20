package com.app.hotelsaas.catin.web.rest.client.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ClientDetailResponse(
        String id,
        String fullName,
        String document,
        String email,
        String phone,
        String status,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {}
