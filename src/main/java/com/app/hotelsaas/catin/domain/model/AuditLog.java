package com.app.hotelsaas.catin.domain.model;

import com.app.hotelsaas.catin.domain.enums.ActionEnum;
import com.app.hotelsaas.catin.domain.enums.EntityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    private UUID id;
    private ActionEnum action;
    private EntityEnum entity;
    private String entityId;
    private String email;
    private UUID tenantId;
    private LocalDateTime timestamp;
    private String details;

}
