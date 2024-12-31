package com.alex.task.tracker.entity;


import com.alex.task.tracker.listener.AuditEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * This abstract entity class used for auditing process.
 * All the necessary entities extend it for enable auditing.
 *
 * @param <K> id of the auditing entity.
 * @see AuditEntityListener listener.
 * @see BaseEntity base entity.
 */

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditEntityListener.class)
public abstract class AuditEntity<K extends Serializable> implements BaseEntity<K> {
    private Instant createdAt;
    private Instant updatedAt;
}