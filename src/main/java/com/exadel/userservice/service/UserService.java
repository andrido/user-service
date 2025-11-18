package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserMapper;
import com.exadel.userservice.dto.UserRequestDTO;
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

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.convertToEntity(dto);

        if (repository.existsByEmail(user.getEmail())) {
            throw new UserValidationException("Email already exists.");
        }

        validator.validate(user);
        User saved = repository.save(user);

        return userMapper.convertToDTO(saved);
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
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (!existingUser.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new UserValidationException("Email already exists.");
        }


        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhone(dto.getPhone());

        validator.validateUpdate(existingUser);


        User savedUser = repository.save(existingUser);


        return userMapper.convertToDTO(savedUser);
    }



    public List<UserSummaryDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return userMapper.convertToSummaryDTOList(users);
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
