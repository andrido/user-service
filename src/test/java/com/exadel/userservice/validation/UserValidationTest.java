package com.exadel.userservice.validation;

import com.exadel.userservice.exception.UserValidationException;
import com.exadel.userservice.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    void shouldThrowWhenPasswordTooShort() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("123");

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));

        assertEquals("Password must be at least 6 characters long.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        User user = new User();
        user.setEmail(null);
        user.setPassword("123456");

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));

        assertEquals("Email must not be empty.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsEmpty() {
        User user = new User();
        user.setEmail("");
        user.setPassword("123456");

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));

        assertEquals("Email must not be empty.", ex.getMessage());
    }

    @Test
    void shouldNotThrowWhenValid() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("123456");

        assertDoesNotThrow(() -> validator.validate(user));
    }


    @Test
    void shouldThrowOnUpdateWhenPasswordTooShort() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("123");

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validateUpdate(user));

        assertEquals("Password must be at least 6 characters long.", ex.getMessage());
    }

    @Test
    void shouldNotThrowOnUpdateWhenPasswordNull() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword(null);

        assertDoesNotThrow(() -> validator.validateUpdate(user));
    }
}
