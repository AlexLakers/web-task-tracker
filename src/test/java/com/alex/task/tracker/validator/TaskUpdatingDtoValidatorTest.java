package com.alex.task.tracker.validator;


import com.alex.task.tracker.dto.TaskUpdatingDto;
import com.alex.task.tracker.entity.enums.TaskPriority;
import com.alex.task.tracker.entity.enums.TaskStatus;
import org.hibernate.boot.model.process.internal.UserTypeMutabilityPlanAdapter;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskUpdatingDtoValidatorTest {
    private Validator validator;
    static private ValidatorFactory validatorFactory;
    private static final Long ID=1L;

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
    void validateTaskUpdatingDtoWithHibernateValidator_shouldReturnEmptyErrorList_whenFullDto() {
        var dto = TaskUpdatingDto.builder().id(ID).performerId(ID).loggedAccountId(ID)
                .deadLineDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .description("desc").priority(TaskPriority.MDM.name()).status(TaskStatus.PROGRESS.name()).build();


        Set<ConstraintViolation<TaskUpdatingDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
    @Test
    void validateTaskUpdatingDtoWithHibernateValidator_shouldReturnEmptyErrorList_whenEmptyDto() {
        var dto = TaskUpdatingDto.builder().id(ID).loggedAccountId(ID).build();

        Set<ConstraintViolation<TaskUpdatingDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
    @ParameterizedTest
    @MethodSource("getNotValidArgs")
    void validateTaskUpdatingDtoWithHibernateValidator_shouldReturnNotEmptyErrorList_whenErrorsExist(TaskUpdatingDto dto,String message) {
        var violations=validator.validate(dto);

        assertThat(violations).isNotEmpty().hasSize(1);

        assertThat(violations.iterator().next().getMessage())
                .isEqualTo(message);

    }

    static Stream<Arguments> getNotValidArgs(){
        var deadLineDate=LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Stream.of(
                Arguments.of(TaskUpdatingDto.builder().performerId(ID).loggedAccountId(ID)
                        .deadLineDate(deadLineDate)
                        .description("desc").priority(TaskPriority.MDM.name()).status(TaskStatus.PROGRESS.name()).build()
                ,"must not be null"),

                Arguments.of(TaskUpdatingDto.builder().id(ID).performerId(ID)
                                .deadLineDate(deadLineDate)
                                .description("desc").priority(TaskPriority.MDM.name()).status(TaskStatus.PROGRESS.name()).build()
                 ,"must not be null"),

                Arguments.of(TaskUpdatingDto.builder().id(ID).performerId(ID).loggedAccountId(ID)
                          .deadLineDate("1993-01").description("desc").priority(TaskPriority.MDM.name()).status(TaskStatus.PROGRESS.name()).build()
                ,"The date should have the following format yyyy-mm-dd"),

                Arguments.of(TaskUpdatingDto.builder().id(ID).performerId(ID).loggedAccountId(ID)
                                .deadLineDate(deadLineDate).description("desc").priority("InvalidPriority").status(TaskStatus.PROGRESS.name()).build()
                        ,"The priority of task should be 'LOW','HGH','MDM' or empty"),

                Arguments.of(TaskUpdatingDto.builder().id(ID).performerId(ID).loggedAccountId(ID)
                                .deadLineDate(deadLineDate).description("desc").priority(TaskPriority.MDM.name()).status("InvalidStatus").build()
                        ,"The status of task should be 'PROGRESS','PERFORMED','CANCELLED' or empty")

        );

    }
}