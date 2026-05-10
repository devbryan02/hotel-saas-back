package com.app.hotelsaas.catin.infrastructure.audit;

import com.app.hotelsaas.catin.domain.model.AuditLog;
import com.app.hotelsaas.catin.domain.port.AuditLogRepository;
import com.app.hotelsaas.catin.infrastructure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {

        Object result = pjp.proceed();

        try {
            AuditLog auditLog = buildAuditLog(auditable, result);
            auditLogRepository.save(auditLog);
            log.info("[AUDIT] {} on {} | user: {} | tenant: {}",
                    auditLog.getAction(), auditLog.getEntity(),
                    auditLog.getEmail(), auditLog.getTenantId());
        } catch (Exception e) {
            log.error("[AUDIT] Failed to save audit log for action={} entity={}: {}",
                    auditable.action(), auditable.entity(), e.getMessage());
        }

        return result;
    }

    private AuditLog buildAuditLog(Auditable auditable, Object result) {
        CustomUserDetails currentUser = extractCurrentUser();

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(auditable.action());
        auditLog.setEntity(auditable.entity());
        auditLog.setEntityId(extractEntityId(result));
        auditLog.setEmail(currentUser != null ? currentUser.getUsername() : "anonymous");
        auditLog.setTenantId(currentUser != null ? currentUser.getTenantId() : null);
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails("result_type=" + (result != null ? result.getClass().getSimpleName() : "void"));
        return auditLog;
    }

    private CustomUserDetails extractCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails;
        }
        return null;
    }

    private String extractEntityId(Object result) {
        if (result == null) return null;
        try {
            var method = result.getClass().getMethod("getId");
            Object id = method.invoke(result);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}