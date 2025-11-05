package com.exadel.userservice.common.event;

import lombok.Data;
@Data
public class LoanEvent {
    private String bookId;
    private String userId;
    private String status;
    private String bookTitle;
}

