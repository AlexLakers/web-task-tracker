package com.alex.task.tracker.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Base64;

/**
 * This class includes the methods for hiding yoy real pass.
 *
 * @see AttributeConverter
 */

@Converter
public class PasswordAttributeConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    @Override
    public String convertToEntityAttribute(String stringPassword) {
        return new String(Base64.getDecoder().decode(stringPassword));
    }
}