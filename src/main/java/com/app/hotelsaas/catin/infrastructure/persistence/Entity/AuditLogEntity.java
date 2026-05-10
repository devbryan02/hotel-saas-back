package com.app.hotelsaas.catin.infrastructure.persistence.Entity;

import com.app.hotelsaas.catin.domain.enums.ActionEnum;
import com.app.hotelsaas.catin.domain.enums.EntityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit_log")
public class AuditLogEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private ActionEnum action;
    @Enumerated(EnumType.STRING)
    private EntityEnum entity;
    private String entityId;
    private String email;
    private UUID tenantId;
    private LocalDateTime timestamp;
    private String details;


}
