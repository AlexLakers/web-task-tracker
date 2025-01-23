package com.alex.task.tracker.exception.task;


import com.alex.task.tracker.service.TaskService;

/**
 * This exception  represents some error which throws during search the task by filter.
 *
 * @see TaskService#findById(Long id) search the task.
 */

public class TasksFilteringException extends RuntimeException {
    public TasksFilteringException(String message) {
        super(message);
    }

    public TasksFilteringException(String message, Throwable cause) {
        super(message, cause);
    }

    public TasksFilteringException(Throwable cause) {
        super(cause);
    }
}
