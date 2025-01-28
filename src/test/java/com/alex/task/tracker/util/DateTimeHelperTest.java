package com.alex.task.tracker.util;

import static org.junit.jupiter.api.Assertions.*;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeHelperTest {

    @ParameterizedTest
    @ValueSource(strings = {"1993-01","2011","0-0---0"})
    void parseDate_shouldThrowDateTimeParseException_whenNotParsed(String invalid){
        assertThrowsExactly(DateTimeParseException.class,()->DateTimeHelper.parseDateTime(invalid));
    }
    @NullAndEmptySource
    @ParameterizedTest
    void parseDate_shouldThrowNullPointerException_whenArgNullOrEmpty(String invalid){
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> DateTimeHelper.parseDateTime(invalid))
                .withMessage("dateTime is null or empty");
    }
    @ParameterizedTest
    @ValueSource(strings = {"1993-01","2011","0-0---0"})
    void parseTime_shouldThrowDateTimeParseException_whenNotParsed(String invalid){
        assertThrowsExactly(DateTimeParseException.class,()->DateTimeHelper.parseTime(invalid));

    }
    @NullAndEmptySource
    @ParameterizedTest
    void parseTime_shouldThrowNullPointerException_whenArgNullOrEmpty(String invalid){
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> DateTimeHelper.parseTime(invalid))
                .withMessage("time is null or empty");
    }
    @ParameterizedTest
    @ValueSource(strings = {"1993-01","2011","0-0---0"})
    void parseDateTime_shouldThrowIllegalArgumentException_whenNotParsed(String invalid){
        assertThrowsExactly(DateTimeParseException.class,()->DateTimeHelper.parseDateTime(invalid));

    }

    @NullAndEmptySource
    @ParameterizedTest
    void parseDateTime_shouldThrowIllegalArgumentException_whenArgNullOrEmpty(String invalid){
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> DateTimeHelper.parseTime(invalid))
                .withMessage("time is null or empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1993-01-21","2011-01-01","2022-12-12"})
    void dateIsValid_shouldReturnTrue_whenDateIsValidByPattern(String valid) {
        boolean actual=DateTimeHelper.dateIsValid("1993-01-21");

        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1993-01-21 21:01:10","2011-01","2011-14-14"})
    void dateIsValid_shouldReturnFalse_whenDateIsNotValidByPattern(String invalid) {
        boolean actual=DateTimeHelper.dateIsValid(invalid);

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getArgsForTime")
    void timeIsValid_shouldReturnTrue_whenDateIsValidByPattern(String sTime,boolean expected) {
        boolean actual=DateTimeHelper.timeIsValid(sTime);

        assertThat(actual).isEqualTo(expected);
    }
    @ParameterizedTest
    @MethodSource("getArgsForDateTime")
    void dateTimeIsValid_shouldReturnTrue_whenDateIsValidByPattern(String sDateTime,boolean expected) {
        boolean actual=DateTimeHelper.DateTimeIsValid(sDateTime);

        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getArgsForDateTime(){
        return Stream.of(
                Arguments.of("1993-01-01 10:10:10",true),
                Arguments.of("2011-02-11 12:33:00",true),
                Arguments.of("1993-01-01",false),
                Arguments.of("12:33:00",false)
        );
    }

    static Stream<Arguments> getArgsForTime(){
        return Stream.of(
                Arguments.of("10:10:10",true),
                Arguments.of("12:33:00",true),
                Arguments.of("1993-01-01",false),
                Arguments.of("1-1-1",false)
        );
    }
}