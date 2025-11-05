package com.exadel.userservice.validation;

import com.exadel.userservice.exception.UserValidationException;
import com.exadel.userservice.model.User;
import com.exadel.userservice.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository repository;

    public UserValidator(UserRepository repository) {
        this.repository = repository;
    }

    public void validate(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserValidationException("Email already exists.");
        }

        if (user.getPassword().length() < 6) {
            throw new UserValidationException("Password must be at least 6 characters long.");
        }
    }

    public void validateUpdate(User user) {
        User existing = repository.findByEmail(user.getEmail()).orElse(null);
        if (existing != null && existing.getId() != user.getId()) {
            throw new UserValidationException("Email already exists for another user.");
        }

        if (user.getPassword() != null && user.getPassword().length() < 6) {
            throw new UserValidationException("Password must be at least 6 characters long.");
        }
    }

}
