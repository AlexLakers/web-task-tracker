package com.alex.task.tracker.validator;

import com.alex.task.tracker.dto.AccountCreationDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountCreationDtoValidatorTest {
    private Validator validator;
    static private ValidatorFactory validatorFactory;

    @BeforeAll
    static void setValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @BeforeEach
    void setValidator() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @AfterEach
    void closeValidator(){
        validatorFactory.close();
    }


    @Test
    void validateCreationDtoWithHibernateValidator_shouldReturnEmptyErrorList_whenErrorsAreNotExist() {
        var dto = AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build();

        Set<ConstraintViolation<AccountCreationDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
    @ParameterizedTest
    @MethodSource("getNotValidArgs")
    void validateCreationDtoWithHibernateValidator_shouldReturnNotEmptyErrorList_whenErrorsExist(AccountCreationDto dto,String message) {
        var violations=validator.validate(dto);

        assertThat(violations).isNotEmpty().hasSize(1);

        assertThat(violations.iterator().next().getMessage())
                .isEqualTo(message);

    }

    @Test
    void validateCreationDtoWithHibernateValidator_shouldReturnFullErrorList_whenDtoIsEmpty() {
        var emptyDto=AccountCreationDto.builder().build();
        var violations=validator.validate(emptyDto);

        assertThat(violations).isNotEmpty().hasSize(emptyDto.getClass().getDeclaredFields().length+1);
    }
    static Stream<Arguments> getNotValidArgs() {
        return Stream.of(
                Arguments.of(AccountCreationDto.builder().login("Alex").firstName("alex")
                        .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build(),
                        "The login should be as an email format"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("a")
                        .lastName("Alexov").password("Alex123a").birthday("1993-01-01").build(),
                        "The firstname should contains more symbols"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("A").password("Alex123a").birthday("1993-01-01").build(),
                        "The lastname should contains more symbols"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("A").password("Alex123a").birthday("1993-01-01").build(),
                        "The lastname should contains more symbols"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("Alexov").password("Alex123a").birthday("1993").build(),
                        "The date should have the following format yyyy-mm-dd"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("Alexov").password("Alex123a").birthday("").build(),
                        "The date should have the following format yyyy-mm-dd"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("Alexov").password("12345").birthday("1993-01-01").build(),
                        "Password should contains digits and big letters"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("Alexov").password("12345aa").birthday("1993-01-01").build(),
                        "Password should contains digits and big letters"),

                Arguments.of(AccountCreationDto.builder().login("Alex@mail").firstName("alex")
                                .lastName("Alexov").password("").birthday("1993-01-01").build(),
                        "Password should contains digits and big letters")




        );
               /* Arguments.of("1993-01-01 10:10:10", false),
                Arguments.of("1993-01", false),
                Arguments.of("2011-01-01", true)
        );*/
    }

    }