package com.exadel.userservice.dto;

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
    private String status; // BORROWED ou RETURNED
}
