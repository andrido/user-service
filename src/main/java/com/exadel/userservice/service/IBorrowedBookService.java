package com.exadel.userservice.service;

import com.exadel.userservice.dto.BookStatus;
import com.exadel.userservice.model.BorrowedStatus;

public interface IBorrowedBookService {
    void handleBookEvent(Long userId, Long bookId, String bookTitle, BookStatus status, BorrowedStatus borrowedStatus);
}
