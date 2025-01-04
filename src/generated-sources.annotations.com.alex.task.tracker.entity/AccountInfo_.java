package com.alex.task.tracker.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AccountInfo.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class AccountInfo_ {

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";

	
	/**
	 * @see com.alex.task.tracker.entity.AccountInfo#firstName
	 **/
	public static volatile SingularAttribute<AccountInfo, String> firstName;
	
	/**
	 * @see com.alex.task.tracker.entity.AccountInfo#lastName
	 **/
	public static volatile SingularAttribute<AccountInfo, String> lastName;
	
	/**
	 * @see com.alex.task.tracker.entity.AccountInfo
	 **/
	public static volatile EmbeddableType<AccountInfo> class_;

}

