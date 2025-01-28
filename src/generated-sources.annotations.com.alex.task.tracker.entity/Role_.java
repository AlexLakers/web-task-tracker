package com.alex.task.tracker.entity;

import com.alex.task.tracker.entity.enums.RoleName;
import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Role.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Role_ extends com.alex.task.tracker.entity.AuditEntity_ {

	public static final String GRAPH_ROLE_PERMISSIONS = "Role.permissions";
	public static final String PERMISSIONS = "permissions";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String ACCOUNTS = "accounts";

	
	/**
	 * @see com.alex.task.tracker.entity.Role#permissions
	 **/
	public static volatile ListAttribute<Role, String> permissions;
	
	/**
	 * @see com.alex.task.tracker.entity.Role#name
	 **/
	public static volatile SingularAttribute<Role, RoleName> name;
	
	/**
	 * @see com.alex.task.tracker.entity.Role#id
	 **/
	public static volatile SingularAttribute<Role, Integer> id;
	
	/**
	 * @see com.alex.task.tracker.entity.Role#accounts
	 **/
	public static volatile ListAttribute<Role, Account> accounts;
	
	/**
	 * @see com.alex.task.tracker.entity.Role
	 **/
	public static volatile EntityType<Role> class_;

}

