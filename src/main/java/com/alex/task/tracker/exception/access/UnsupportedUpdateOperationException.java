package com.alex.task.tracker.exception.access;


import com.alex.task.tracker.exception.ServiceException;

/**
 * This exception describes error during updating some task without necessary permissions.
 */

public class UnsupportedUpdateOperationException extends ServiceException {
    public UnsupportedUpdateOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedUpdateOperationException(String message) {
        super(message);
    }

    public UnsupportedUpdateOperationException(Throwable cause) {
        super(cause);
    }
}
