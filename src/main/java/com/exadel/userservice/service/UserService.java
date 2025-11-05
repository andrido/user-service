package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserMapper;
import com.exadel.userservice.dto.UserResponseDTO;
import com.exadel.userservice.dto.UserSummaryDTO;
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
public class UserService {

    private final UserRepository repository;
    private final UserValidator validator;
    private final BorrowedBookRepository borrowedBookRepository;
    private final UserMapper userMapper;

    public User createUser(User user) {
        validator.validate(user);
        return repository.save(user);
    }

    // Pega um usuário específico e os livros emprestados
    public UserResponseDTO getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<BorrowedBook> books = borrowedBookRepository.findByUserId(user.getId());

        // Popula a lista de borrowedBooks do usuário
        user.setBorrowedBooks(books);

        List<String> titles = books.stream()
                .filter(b -> b.getStatus() == BorrowedStatus.BORROWED)
                .map(BorrowedBook::getBookTitle)
                .toList();

        return userMapper.convertToDTO(user, titles);
    }


    public List<UserSummaryDTO> getAllUsers() {
        List<User> users = repository.findAll();
        return userMapper.convertToSummaryDTOList(users);
    }


    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
