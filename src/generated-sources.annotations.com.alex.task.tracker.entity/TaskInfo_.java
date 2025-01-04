package com.alex.task.tracker.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(TaskInfo.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class TaskInfo_ {

	public static final String DEADLINE_DATE = "deadlineDate";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String CREATION_DATE = "creationDate";

	
	/**
	 * @see com.alex.task.tracker.entity.TaskInfo#deadlineDate
	 **/
	public static volatile SingularAttribute<TaskInfo, LocalDate> deadlineDate;
	
	/**
	 * @see com.alex.task.tracker.entity.TaskInfo#name
	 **/
	public static volatile SingularAttribute<TaskInfo, String> name;
	
	/**
	 * @see com.alex.task.tracker.entity.TaskInfo#description
	 **/
	public static volatile SingularAttribute<TaskInfo, String> description;
	
	/**
	 * @see com.alex.task.tracker.entity.TaskInfo#creationDate
	 **/
	public static volatile SingularAttribute<TaskInfo, LocalDate> creationDate;
	
	/**
	 * @see com.alex.task.tracker.entity.TaskInfo
	 **/
	public static volatile EmbeddableType<TaskInfo> class_;

}

