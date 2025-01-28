package com.alex.task.tracker.exception.task;


import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.service.TaskService;

/**
 * This exception  represents some error which throws during creation a new task.
 *
 * @see TaskService#create(TaskCreationDto dto) create a new task.
 */

public class TaskCreationException extends ServiceException {
    public TaskCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskCreationException(String message) {
        super(message);
    }

    public TaskCreationException(Throwable cause) {
        super(cause);
    }
}
