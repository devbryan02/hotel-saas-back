package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private UUID id;
    private Tenant tenant;
    private String fullName;
    private String document;
    private String email;
    private String phone;
    private String status;
    private LocalDateTime createdAt;
    
    public static Client create(Tenant tenant, String fullName, String document, String email, String phone){
        return new Client(null, tenant, fullName, document, email, phone, "ACTIVE", LocalDateTime.now());
    }

}