package com.alex.task.tracker.exception;

/**
 * This exception describes the situation then requested account is not available in the database.
 */

public class AccountNotExistException extends ServiceException{
    public AccountNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotExistException(String message) {
        super(message);
    }

    public AccountNotExistException(Throwable cause) {
        super(cause);
    }
}
