package com.alex.task.tracker.dto;



import com.alex.task.tracker.service.AccountService;
import com.alex.task.tracker.validator.ValidDateFormat;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * This is POJO for interaction between different layers of app.
 * It used during creation a new account into {@link AccountService service}.
 *
 * @param firstName entered account firstname.
 * @param lastName entered account lastname.
 * @param login entered account login.
 * @param birthday entered account birthday.
 * @param password entered account password.
 */
@Builder
public record AccountCreationDto(@NotNull
                                 @Pattern(regexp = "[A-Za-z]{2,20}",
                                         message = "The firstname should contains more symbols")
                                 String firstName,

                                 @NotNull @Pattern(regexp = "[A-Za-z]{2,20}",
                                         message = "The lastname should contains more symbols")
                                 String lastName,

                                 @NotNull @Email(message = "The login should be as an email format")
                                 String login,

                                 @NotNull @ValidDateFormat(message = "The date should have the following format yyyy-mm-dd")
                                 String birthday,

                                 @NotNull @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,12}",
                                         message = "Password should contains digits and big letters")
                                 String password

) {
}