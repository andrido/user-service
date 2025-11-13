package com.exadel.userservice.validation;

import com.exadel.userservice.exception.UserValidationException;
import com.exadel.userservice.model.User;
import com.exadel.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("123456");

        when(repository.existsByEmail("test@example.com")).thenReturn(true);

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));
        assertEquals("Email already exists.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordTooShort() {
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("123");

        when(repository.existsByEmail("new@example.com")).thenReturn(false);

        UserValidationException ex = assertThrows(UserValidationException.class,
                () -> validator.validate(user));
        assertEquals("Password must be at least 6 characters long.", ex.getMessage());
    }

    @Test
    void shouldNotThrowWhenValid() {
        User user = new User();
        user.setEmail("new@example.com");
        user.setPassword("123456");

        when(repository.existsByEmail("new@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(user));
    }
}
