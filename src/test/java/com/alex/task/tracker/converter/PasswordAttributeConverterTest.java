package com.alex.task.tracker.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordAttributeConverterTest {
    private PasswordAttributeConverter converter;
    private static final String DEC_PASSWORD = "password";
    private static final String ENC_PASSWORD = "cGFzc3dvcmQ=";

    @BeforeEach
    void setUp() {
        converter = new PasswordAttributeConverter();
    }

    @Test
    void convertToDatabaseColumn_shouldReturnEncPassword() {;
        var actual=converter.convertToDatabaseColumn(DEC_PASSWORD);

        assertThat(actual).isEqualTo(ENC_PASSWORD);
    }
    @Test
    void convertToEntityAttribute_shouldReturnDecPassword() {
        var actual=converter.convertToEntityAttribute(ENC_PASSWORD);

        assertThat(actual).isEqualTo(DEC_PASSWORD);
    }

}