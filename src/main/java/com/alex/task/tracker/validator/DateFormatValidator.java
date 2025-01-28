package com.alex.task.tracker.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This my own validator by JSR 303. It helps to validate the date as a string
 * in the input data (dto).You need to mark
 * a necessary field {@link ValidDateFormat annotation}.
 * And then to transmit checking date and additional params.
 *
 * @see javax.validation.ConstraintValidator validator.
 */

public class DateFormatValidator implements javax.validation.ConstraintValidator<ValidDateFormat, String> {
    private boolean isUpdating;
    private String pattern;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        this.isUpdating = constraintAnnotation.isUpdating();
        this.pattern = constraintAnnotation.pattern();
        javax.validation.ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String sDate, javax.validation.ConstraintValidatorContext constraintValidatorContext) {
        if (sDate == null || sDate.isEmpty()) {
            return isUpdating;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate.parse(sDate, dtf);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}



