package com.exadel.userservice.service;

import com.exadel.userservice.dto.BookStatus;
import com.exadel.userservice.model.BorrowedBook;
import com.exadel.userservice.model.BorrowedStatus;
import com.exadel.userservice.model.User;
import com.exadel.userservice.repository.BorrowedBookRepository;
import com.exadel.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowedBookService implements IBorrowedBookService {

    private final BorrowedBookRepository repository;
    private final UserRepository userRepository;

    @Override
    public void handleBookEvent(Long userId, Long bookId, String bookTitle, BookStatus status, BorrowedStatus borrowedStatus) {
        if (borrowedStatus == BorrowedStatus.BORROWED) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            BorrowedBook borrowedBook = BorrowedBook.builder()
                    .bookId(bookId)
                    .bookTitle(bookTitle)
                    .status(BorrowedStatus.BORROWED)
                    .eventAt(LocalDateTime.now())
                    .user(user)
                    .build();

            repository.save(borrowedBook);

        } else if (borrowedStatus == BorrowedStatus.RETURNED) {
            BorrowedBook borrowedBook = repository.findByUserIdAndBookIdAndStatus(userId, bookId, BorrowedStatus.BORROWED);
            if (borrowedBook != null) {
                borrowedBook.setStatus(BorrowedStatus.RETURNED);
                borrowedBook.setEventAt(LocalDateTime.now());
                repository.save(borrowedBook);
            }
        }
    }
}