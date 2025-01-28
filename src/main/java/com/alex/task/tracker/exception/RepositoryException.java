package com.alex.task.tracker.exception;

/**
 * This exception describes a situation when the requested account is unavailable in the database.
 */

public class RepositoryException extends RuntimeException{
    public RepositoryException(String message, Throwable cause){
        super(message,cause);
    }
    public RepositoryException(String message){
        super(message);
    }
}
