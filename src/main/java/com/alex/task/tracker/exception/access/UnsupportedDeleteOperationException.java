package com.alex.task.tracker.exception.access;


import com.alex.task.tracker.exception.ServiceException;

/**
 * This exception describes error during deleting some task without necessary permissions.
 */

public class UnsupportedDeleteOperationException extends ServiceException {
    public UnsupportedDeleteOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedDeleteOperationException(String message) {
        super(message);
    }

    public UnsupportedDeleteOperationException(Throwable cause) {
        super(cause);
    }
}
