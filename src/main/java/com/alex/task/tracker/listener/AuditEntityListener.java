package com.alex.task.tracker.listener;



import com.alex.task.tracker.entity.AuditEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

/**
 * This is a listener that allows to fill special fields for auditing.
 */

public class AuditEntityListener {

    @PrePersist
    public void prePersist(AuditEntity<?> auditEntity) {
        auditEntity.setCreatedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate(AuditEntity<?> auditEntity) {
        auditEntity.setUpdatedAt(Instant.now());
    }
}