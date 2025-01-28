package com.alex.task.tracker.validator;

import com.alex.task.tracker.dto.TaskCreationDto;
import com.alex.task.tracker.entity.enums.TaskPriority;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskCreationDtoValidatorTest {
    private Validator validator;
    static private ValidatorFactory validatorFactory;
    private static final Long ID = 1L;
    private final static DateTimeFormatter DEFAULT_PATTERN=DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static String DESC_TASK="DESCCCCCCCCCCCCCCCCCCCCCCC";

    @BeforeAll
    static void setValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @BeforeEach
    void setValidator() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterEach
    void closeValidator() {
        validatorFactory.close();
    }


    @Test
    void validateTaskCreationDtoWithHibernateValidator_shouldReturnEmptyErrorList_whenFullDto() {
        var dto = TaskCreationDto.builder().performerId(ID).loggedAccountId(ID).name("TASK")
                .deadLineDate(LocalDate.now().plusDays(1).format(DEFAULT_PATTERN))
                .description("dddddeeeeessssccccrrrriiipptions").priority(TaskPriority.MDM.name()).build();


        Set<ConstraintViolation<TaskCreationDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validateTaskCreationDtoWithHibernateValidator_shouldReturnFullErrorList_whenAllFieldNullDto() {
        var emptyDto = TaskCreationDto.builder().build();
        var countOwnValidator=2;

        Set<ConstraintViolation<TaskCreationDto>> violations = validator.validate(emptyDto);

        assertThat(violations).hasSize(emptyDto.getClass().getDeclaredFields().length+countOwnValidator);
    }

    @ParameterizedTest
    @MethodSource("getNotValidArgs")
    void validateTaskCreationDtoWithHibernateValidator_shouldReturnNotEmptyErrorList_whenErrorsExist(TaskCreationDto dto, String message) {
        var violations = validator.validate(dto);

        assertThat(violations).isNotEmpty().hasSize(1);

        assertThat(violations.iterator().next().getMessage())
                .isEqualTo(message);

    }

    static Stream<Arguments> getNotValidArgs() {
        var deadLineDate = LocalDate.now().plusDays(1).format(DEFAULT_PATTERN);

        return Stream.of(
                Arguments.of(TaskCreationDto.builder().name("t").performerId(ID).loggedAccountId(ID)
                                .deadLineDate(deadLineDate)
                                .description(DESC_TASK).priority(TaskPriority.MDM.name()).build()
                        , "The task name should contains more symbols"),

                Arguments.of(TaskCreationDto.builder().name("task").performerId(ID)
                                .deadLineDate(deadLineDate)
                                .description(DESC_TASK).priority(TaskPriority.MDM.name()).build()
                        , "must not be null"),

                Arguments.of(TaskCreationDto.builder().name("task").loggedAccountId(ID)
                                .deadLineDate(deadLineDate)
                                .description(DESC_TASK).priority(TaskPriority.MDM.name()).build()
                        , "must not be null"),

                Arguments.of(TaskCreationDto.builder().name("TASK").performerId(ID).loggedAccountId(ID)
                                .deadLineDate("1993-01").description(DESC_TASK).priority(TaskPriority.MDM.name()).build()
                        , "The date should have the following format yyyy-mm-dd"),



                Arguments.of(TaskCreationDto.builder().name("task").performerId(ID).loggedAccountId(ID)
                                .deadLineDate(deadLineDate).description(DESC_TASK).priority("InvalidPriority").build()
                        , "The priority of task should be 'LOW','HGH','MDM'")

        );
    }
}