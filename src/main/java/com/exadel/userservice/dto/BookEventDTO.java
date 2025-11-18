package com.exadel.userservice.dto;

import com.exadel.userservice.model.BorrowedBook;
import com.exadel.userservice.model.BorrowedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEventDTO {
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private BorrowedStatus status; // BORROWED ou RETURNED
}
