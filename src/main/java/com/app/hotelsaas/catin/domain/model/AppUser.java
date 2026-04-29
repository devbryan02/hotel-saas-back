package com.app.hotelsaas.catin.domain.model;

import com.app.hotelsaas.catin.domain.enums.RoleEnum;
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
    private RoleEnum role;
    private String status;
    private LocalDateTime createdAt;

    public static AppUser create(Tenant tenant, String email, String passwordHash, RoleEnum role ){
        return new AppUser(null, tenant, email, passwordHash, role, "ACTIVE", LocalDateTime.now()
        );
    }
}