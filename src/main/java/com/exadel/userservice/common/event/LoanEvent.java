package com.exadel.userservice.common.event;

import lombok.Data;
@Data
public class LoanEvent {
    private Long bookId;
    private Long userId;
    private String status;
    private String bookTitle;
}

