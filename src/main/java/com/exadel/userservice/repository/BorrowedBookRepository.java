package com.exadel.userservice.repository;

import com.exadel.userservice.model.BorrowedBook;
import com.exadel.userservice.model.BorrowedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByUserId(Long userId);
    void deleteByBookId(Long bookId);
    void deleteByUserIdAndBookId(Long userId, Long bookId);
    BorrowedBook findByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowedStatus status);
    List<BorrowedBook> findByUserIdAndStatus(Long userId, BorrowedStatus status);

}
