package com.exadel.userservice.kafka;

import com.exadel.userservice.dto.BookEventDTO;
import com.exadel.userservice.model.BorrowedBook;
import com.exadel.userservice.repository.BorrowedBookRepository;
import com.exadel.userservice.service.BorrowedBookService;
import com.exadel.userservice.service.IBorrowedBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanEventConsumer {

    private final IBorrowedBookService borrowedBookService;

    @KafkaListener(topics = "loan-events", groupId = "user-service-group")
    public void consume(BookEventDTO event) {
        borrowedBookService.handleBookEvent(
                event.getUserId(),
                event.getBookId(),
                event.getBookTitle(),
                null, // BookStatus se quiser ignorar
                event.getStatus() // BorrowedStatus direto
        );
    }
    }

