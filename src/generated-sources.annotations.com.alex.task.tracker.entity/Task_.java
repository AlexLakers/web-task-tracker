package com.alex.task.tracker.entity;

import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Task.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Task_ extends com.alex.task.tracker.entity.AuditEntity_ {

	public static final String NUMBER = "number";
	public static final String PERFORMER = "performer";
	public static final String ID = "id";
	public static final String TASK_INFO = "taskInfo";
	public static final String PRIORITY = "priority";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

	
	/**
	 * @see com.alex.task.tracker.entity.Task#number
	 **/
	public static volatile SingularAttribute<Task, Integer> number;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#performer
	 **/
	public static volatile SingularAttribute<Task, Account> performer;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#id
	 **/
	public static volatile SingularAttribute<Task, Long> id;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#taskInfo
	 **/
	public static volatile SingularAttribute<Task, TaskInfo> taskInfo;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#priority
	 **/
	public static volatile SingularAttribute<Task, TaskPriority> priority;
	
	/**
	 * @see com.alex.task.tracker.entity.Task
	 **/
	public static volatile EntityType<Task> class_;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#customer
	 **/
	public static volatile SingularAttribute<Task, Account> customer;
	
	/**
	 * @see com.alex.task.tracker.entity.Task#status
	 **/
	public static volatile SingularAttribute<Task, TaskStatus> status;

}

