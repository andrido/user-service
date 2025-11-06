package com.exadel.userservice.service;

public interface IBorrowedBookService {
    void handleBookEvent(Long userId, Long bookId, String bookTitle, String status);
}
