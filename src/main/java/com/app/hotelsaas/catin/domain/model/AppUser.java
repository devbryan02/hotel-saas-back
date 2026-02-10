package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    private UUID id;
    private Tenant tenant;
    private String email;
    private String passwordHash;
    private String role;
    private String status;
    private LocalDateTime createdAt;

}