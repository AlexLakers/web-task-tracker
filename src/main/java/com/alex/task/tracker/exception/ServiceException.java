package com.alex.task.tracker.exception;
/**
 * This exception describes some general exception that occurs in a service layer in this app.
 * It represents a parent exception for other exceptions which can be used on a service layer.
 */

public class ServiceException extends RuntimeException{
    public ServiceException(String message,Throwable cause) {
        super(message,cause);
    }
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
