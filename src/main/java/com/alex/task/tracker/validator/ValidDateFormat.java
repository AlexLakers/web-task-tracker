package com.alex.task.tracker.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is a part my own validator by JSR 303.
 *
 * @see DateFormatValidator validator.
 */

@Constraint(validatedBy = {DateFormatValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateFormat {
    boolean isUpdating() default false;

    String pattern() default "yyyy-MM-dd";

    String message() default "date error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
