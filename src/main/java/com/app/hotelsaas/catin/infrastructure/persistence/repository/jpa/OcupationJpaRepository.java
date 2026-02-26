package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OcupationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OcupationJpaRepository extends JpaRepository<OcupationEntity, UUID> {
}
