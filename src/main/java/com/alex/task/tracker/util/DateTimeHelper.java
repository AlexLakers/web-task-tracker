package com.alex.task.tracker.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This is utility class for checking and parsing operations
 * with date,time or datetime.
 * As you understand we use string representation of date.
 */

@UtilityClass
public class DateTimeHelper {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    public static LocalDate parseDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("date is null or empty");
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static LocalTime parseTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("time is null or empty");
        }
        return LocalTime.parse(time.trim(), DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            throw new IllegalArgumentException("dateTime is null or empty");
        }
        return LocalDateTime.parse(dateTime.trim(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public static boolean dateIsValid(String date) {
        try {
            parseDate(date);
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean timeIsValid(String time) {
        try {
            parseTime(time);
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean DateTimeIsValid(String dateTime) {
        try {
            parseDateTime(dateTime);
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return false;
        }
        return true;
    }


}