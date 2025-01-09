package com.alex.task.tracker.exception.auth;


import com.alex.task.tracker.dto.AccountCreationDto;
import com.alex.task.tracker.exception.ServiceException;
import com.alex.task.tracker.service.AccountService;

/**
 * This exception describes registration error during registration a new account.
 *
 * @see AccountService#createAccount(AccountCreationDto dto) registartion account.
 */

public class AccountRegistrationException extends ServiceException {
    public AccountRegistrationException(String message) {
        super(message);
    }

    public AccountRegistrationException(Throwable cause) {
        super(cause);
    }

    public AccountRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
