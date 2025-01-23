package com.alex.task.tracker.validator;

import com.alex.task.tracker.entity.enums.TaskStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This my own validator by JSR 303.
 * It helps to validate the status task as a string
 * in the input data (dto).
 * You need to mark a necessary field with status the following {@link ValidStatusTask annotation}.
 * And then to transmit checking status and additional params.
 *
 * @see ConstraintValidator validator.
 */

public class StatusTaskValidator implements ConstraintValidator<ValidStatusTask, String> {
    @Override
    public void initialize(ValidStatusTask constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String sStatus, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (sStatus == null || sStatus.isEmpty()) return true;
            TaskStatus.valueOf(sStatus);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
