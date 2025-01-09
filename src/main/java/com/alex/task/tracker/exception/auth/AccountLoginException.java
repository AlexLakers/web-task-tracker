package com.alex.task.tracker.exception.auth;


import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.service.AccountService;


/**
 * This exception describes authentication error during log in process.
 *
 * @see AccountService#loginAccount(String login, String pass) logg in account.
 */

public class AccountLoginException extends ServiceException {
    public AccountLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountLoginException(String message) {
        super(message);
    }

    public AccountLoginException(Throwable cause) {
        super(cause);
    }
}
