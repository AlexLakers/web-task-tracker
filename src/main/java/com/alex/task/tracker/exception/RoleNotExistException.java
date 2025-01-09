package com.alex.task.tracker.exception;
/**
 * This exception describes a situation when the requested role is unavailable in the database.
 */

public class RoleNotExistException extends ServiceException{
    public RoleNotExistException(String message) {
        super(message);
    }
    public RoleNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public RoleNotExistException(Throwable cause) {
        super(cause);
    }

}
