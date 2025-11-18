package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserRequestDTO;
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
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO(); // Preencha com dados se necessário
        User userEntity = new User();
        User savedUser = new User();
        UserResponseDTO responseDTO = new UserResponseDTO();

        // Mock do Mapper (DTO -> Entity)
        when(userMapper.convertToEntity(requestDTO)).thenReturn(userEntity);
        // Mock da verificação de email
        when(userRepository.existsByEmail(any())).thenReturn(false);
        // Mock do repositório
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        // Mock do Mapper (Entity -> DTO)
        when(userMapper.convertToDTO(savedUser)).thenReturn(responseDTO);

        // Act
        UserResponseDTO result = userService.createUser(requestDTO);

        // Assert
        verify(validator).validate(userEntity);
        verify(userRepository).save(userEntity);
        assertEquals(responseDTO, result);
    }

    @Test
    void createUser_ShouldNotSave_WhenValidationFails() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO();
        User userEntity = new User();

        when(userMapper.convertToEntity(requestDTO)).thenReturn(userEntity);
        when(userRepository.existsByEmail(any())).thenReturn(false);

        // Simula erro na validação
        doThrow(new RuntimeException("invalid")).when(validator).validate(userEntity);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createUser(requestDTO));
        assertEquals("invalid", ex.getMessage());

        verify(validator).validate(userEntity);
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
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setEmail("test@example.com");

        User userEntity = new User();
        userEntity.setEmail("test@example.com");

        when(userMapper.convertToEntity(requestDTO)).thenReturn(userEntity);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        UserValidationException ex = assertThrows(UserValidationException.class, () -> userService.createUser(requestDTO));
        assertEquals("Email already exists.", ex.getMessage());

        verify(userRepository, never()).save(any());
    }


}
