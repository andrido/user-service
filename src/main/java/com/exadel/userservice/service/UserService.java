package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserMapper;
import com.exadel.userservice.dto.UserResponseDTO;
import com.exadel.userservice.dto.UserSummaryDTO;
import com.exadel.userservice.exception.UserValidationException;
import com.exadel.userservice.model.BorrowedBook;
import com.exadel.userservice.model.BorrowedStatus;
import com.exadel.userservice.model.User;
import com.exadel.userservice.repository.BorrowedBookRepository;
import com.exadel.userservice.repository.UserRepository;
import com.exadel.userservice.validation.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final UserValidator validator;
    private final BorrowedBookRepository borrowedBookRepository;
    private final UserMapper userMapper;

    public User createUser(User user) {

        if (repository.existsByEmail(user.getEmail())) {
            throw new UserValidationException("Email already exists.");
        }

        // Valida dados do objeto
        validator.validate(user);

        return repository.save(user);
    }


    public UserResponseDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(user.getId());

        user.setBorrowedBooks(borrowedBooks);

        List<String> titles = borrowedBooks.stream()
                .filter(b -> b.getStatus() == BorrowedStatus.BORROWED)
                .map(BorrowedBook::getBookTitle)
                .toList();

        return userMapper.convertToDTO(user, titles);
    }
    public User updateUser(User user) {
        User existing = repository.findByEmail(user.getEmail()).orElse(null);
        if (existing != null && !existing.getId().equals(user.getId())) {
            throw new UserValidationException("Email already exists for another user.");
        }

        validator.validateUpdate(user);
        return repository.save(user);
    }


    public List<UserSummaryDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return userMapper.convertToSummaryDTOList(users);
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
