package com.alex.task.tracker.exception.task;


import com.alex.task.tracker.exception.ServiceException;

/**
 * This exception  represents some error which throws during creation a new task.
 *
 * @see com.alex.task.tracker.service.TaskService#delete(Long id, Long userId) delete the task.
 */

public class TaskDeletingException extends ServiceException {
    public TaskDeletingException(Throwable cause) {
        super(cause);
    }

    public TaskDeletingException(String message) {
        super(message);
    }

    public TaskDeletingException(String message, Throwable cause) {
        super(message, cause);
    }
}
