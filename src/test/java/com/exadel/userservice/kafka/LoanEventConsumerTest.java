package com.exadel.userservice.kafka;

import com.exadel.userservice.dto.BookEventDTO;
import com.exadel.userservice.dto.BookStatus;
import com.exadel.userservice.model.BorrowedStatus;
import com.exadel.userservice.service.IBorrowedBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoanEventConsumerTest {

    @Mock
    private IBorrowedBookService borrowedBookService;

    @InjectMocks
    private LoanEventConsumer consumer;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void consume_ShouldCallHandleBookEvent() {
        BookEventDTO event = new BookEventDTO();
        event.setUserId(1L);
        event.setBookId(2L);
        event.setBookTitle("Clean Code");
        event.setStatus(BorrowedStatus.BORROWED);

        consumer.consume(event);

        verify(borrowedBookService).handleBookEvent(
                1L,
                2L,
                "Clean Code",
                null, // BookStatus ignorado aqui
                BorrowedStatus.BORROWED
        );
        ;
        ;
    }
}
