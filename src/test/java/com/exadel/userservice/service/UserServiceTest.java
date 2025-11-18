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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BorrowedBookRepository borrowedBookRepository;

    @Mock
    private UserValidator validator;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldValidateAndSave() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);

        verify(validator).validate(user);
        verify(userRepository).save(user);
        assertEquals(user, saved);
    }

    @Test
    void createUser_ShouldNotSave_WhenValidationFails() {
        User invalid = new User();
        doThrow(new RuntimeException("invalid")).when(validator).validate(invalid);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(invalid));
        assertEquals("invalid", ex.getMessage());

        verify(validator).validate(invalid);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_ShouldReturnUserWithBooks() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        BorrowedBook book = BorrowedBook.builder()
                .bookTitle("Java 101")
                .status(BorrowedStatus.BORROWED)
                .build();
        List<BorrowedBook> books = List.of(book);
        List<String> titles = List.of("Java 101");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(borrowedBookRepository.findByUserId(userId)).thenReturn(books);
        when(userMapper.convertToDTO(user, titles)).thenReturn(new UserResponseDTO());

        UserResponseDTO dto = userService.getUserById(userId);

        assertNotNull(dto);
        verify(userRepository).findById(userId);
        verify(borrowedBookRepository).findByUserId(userId);
        verify(userMapper).convertToDTO(user, titles);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertEquals("User not found", ex.getMessage());

        verify(userRepository).findById(userId);
        verifyNoInteractions(borrowedBookRepository, userMapper);
    }

    @Test
    void deleteUser_ShouldCallRepositoryDeleteById() {
        Long userId = 10L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void getAllUsers_ShouldCallRepositoryAndMap() {
        List<User> users = List.of(new User(), new User());
        List<UserSummaryDTO> summaries = List.of(new UserSummaryDTO(), new UserSummaryDTO());

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.convertToSummaryDTOList(users)).thenReturn(summaries);

        List<UserSummaryDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(userMapper).convertToSummaryDTOList(users);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        UserValidationException ex = assertThrows(UserValidationException.class, () -> userService.createUser(user));
        assertEquals("Email already exists.", ex.getMessage());
    }


}
