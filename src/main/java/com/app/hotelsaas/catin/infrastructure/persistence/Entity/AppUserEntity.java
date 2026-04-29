package com.app.hotelsaas.catin.infrastructure.persistence.Entity;

import com.app.hotelsaas.catin.domain.enums.RoleEnum;
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
@Table(name = "app_user")
public class AppUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    private String email;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    private String status;
    private LocalDateTime createdAt;

}