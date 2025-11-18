package com.exadel.userservice.service;

import com.exadel.userservice.dto.BookStatus;
import com.exadel.userservice.model.*;
import com.exadel.userservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BorrowedBookServiceTest {

    @Mock
    private BorrowedBookRepository borrowedBookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowedBookService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void handleBookEvent_Returned_ShouldUpdateExistingBook() {
        // Mock do usuário
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock do livro já emprestado
        BorrowedBook existing = BorrowedBook.builder()
                .bookId(2L)
                .status(BorrowedStatus.BORROWED)
                .build();
        when(borrowedBookRepository.findByUserIdAndBookIdAndStatus(1L, 2L, BorrowedStatus.BORROWED))
                .thenReturn(existing);

        // Chama o método com RETURNED
        service.handleBookEvent(1L, 2L, "Effective Java", null, BorrowedStatus.RETURNED);

        // Verifica que o save foi chamado
        verify(borrowedBookRepository, times(1)).save(existing);

        assertEquals(BorrowedStatus.RETURNED, existing.getStatus());
    }
}