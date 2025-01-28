package com.alex.task.tracker.exception.access;

import com.alex.task.tracker.exception.ServiceException;

/**
 * This exception describes error during creation a new task without necessary permissions.
 */

public class UnsupportedCreateOperationException extends ServiceException {
    public UnsupportedCreateOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedCreateOperationException(String message) {
        super(message);
    }

    public UnsupportedCreateOperationException(Throwable cause) {
        super(cause);
    }
}
