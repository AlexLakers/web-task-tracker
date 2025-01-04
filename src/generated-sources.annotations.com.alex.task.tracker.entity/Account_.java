package com.alex.task.tracker.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(Account.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Account_ extends com.alex.task.tracker.entity.AuditEntity_ {

	public static final String BIRTHDAY = "birthday";
	public static final String ACCOUNT_INFO = "accountInfo";
	public static final String PERFORMER_TASKS = "performerTasks";
	public static final String PASSWORD = "password";
	public static final String ROLES = "roles";
	public static final String CUSTOMER_TASKS = "customerTasks";
	public static final String ID = "id";
	public static final String LOGIN = "login";

	
	/**
	 * @see com.alex.task.tracker.entity.Account#birthday
	 **/
	public static volatile SingularAttribute<Account, LocalDate> birthday;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#accountInfo
	 **/
	public static volatile SingularAttribute<Account, AccountInfo> accountInfo;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#performerTasks
	 **/
	public static volatile ListAttribute<Account, Task> performerTasks;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#password
	 **/
	public static volatile SingularAttribute<Account, String> password;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#roles
	 **/
	public static volatile ListAttribute<Account, Role> roles;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#customerTasks
	 **/
	public static volatile ListAttribute<Account, Task> customerTasks;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#id
	 **/
	public static volatile SingularAttribute<Account, Long> id;
	
	/**
	 * @see com.alex.task.tracker.entity.Account#login
	 **/
	public static volatile SingularAttribute<Account, String> login;
	
	/**
	 * @see com.alex.task.tracker.entity.Account
	 **/
	public static volatile EntityType<Account> class_;

}

