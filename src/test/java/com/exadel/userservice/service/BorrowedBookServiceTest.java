package com.exadel.userservice.service;

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
    void handleBookEvent_Borrowed_ShouldSaveNewBook() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        service.handleBookEvent(1L, 2L, "Effective Java", "BORROWED");

        verify(borrowedBookRepository).save(any(BorrowedBook.class));
    }

    @Test
    void handleBookEvent_Returned_ShouldUpdateExistingBook() {
        BorrowedBook existing = BorrowedBook.builder()
                .bookId(2L)
                .status(BorrowedStatus.BORROWED)
                .build();

        when(borrowedBookRepository.findByUserIdAndBookIdAndStatus(1L, 2L, BorrowedStatus.BORROWED))
                .thenReturn(existing);

        service.handleBookEvent(1L, 2L, "Effective Java", "RETURNED");

        verify(borrowedBookRepository).save(existing);
        assertEquals(BorrowedStatus.RETURNED, existing.getStatus());
    }
}
