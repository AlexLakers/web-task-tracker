package com.alex.task.tracker.exception.task;


import com.alex.task.tracker.exception.ServiceException;

public class TaskNotExistException extends ServiceException {
    public TaskNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotExistException(String message) {
        super(message);
    }

    public TaskNotExistException(Throwable cause) {
        super(cause);
    }
}
