package com.alex.task.tracker.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.MappedSuperclassType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

@StaticMetamodel(AuditEntity.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class AuditEntity_ {

	public static final String CREATED_AT = "createdAt";
	public static final String UPDATED_AT = "updatedAt";

	
	/**
	 * @see com.alex.task.tracker.entity.AuditEntity#createdAt
	 **/
	public static volatile SingularAttribute<AuditEntity, Instant> createdAt;
	
	/**
	 * @see com.alex.task.tracker.entity.AuditEntity
	 **/
	public static volatile MappedSuperclassType<AuditEntity> class_;
	
	/**
	 * @see com.alex.task.tracker.entity.AuditEntity#updatedAt
	 **/
	public static volatile SingularAttribute<AuditEntity, Instant> updatedAt;

}

