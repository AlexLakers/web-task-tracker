package com.alex.task.tracker.exception.task;


import com.alex.task.tracker.dto.TaskUpdatingDto;
import com.alex.task.tracker.service.TaskService;

/**
 * This exception represents some error which throws during creation a new task.
 *
 * @see TaskService#update(TaskUpdatingDto dto) update the task.
 */

public class TaskUpdatingException extends RuntimeException{
    public TaskUpdatingException(String message) {
        super(message);
    }
    public TaskUpdatingException(String message, Throwable cause) {
        super(message, cause);
    }
    public TaskUpdatingException(Throwable cause) {
        super(cause);
    }
}
