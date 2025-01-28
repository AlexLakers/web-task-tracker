package com.alex.task.tracker.validator;


import com.alex.task.tracker.entity.enums.TaskPriority;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This my own validator by JSR 303.
 * It helps to validate the priority task as a string
 * in the input data (dto).
 * You need to mark a necessary field {@link ValidPriorityTask annotation}.
 * And then to transmit checking priority and additional params.
 *
 * @see ConstraintValidator validator.
 */

public class PriorityTaskValidator implements ConstraintValidator<ValidPriorityTask, String> {

    private boolean isUpdating = false;
    @Override
    public void initialize(ValidPriorityTask constraintAnnotation) {
        this.isUpdating=constraintAnnotation.isUpdatingCheck();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String sPriority, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (sPriority == null) {
                return isUpdating;
            }
            TaskPriority.valueOf(sPriority);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
