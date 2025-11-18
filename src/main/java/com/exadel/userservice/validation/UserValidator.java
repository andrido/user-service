package com.exadel.userservice.validation;

import com.exadel.userservice.exception.UserValidationException;
import com.exadel.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(User user) {
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new UserValidationException("Password must be at least 6 characters long.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserValidationException("Email must not be empty.");
        }
    }

    public void validateUpdate(User user) {
        if (user.getPassword() != null && user.getPassword().length() < 6) {
            throw new UserValidationException("Password must be at least 6 characters long.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserValidationException("Email must not be empty.");
        }
    }
}
