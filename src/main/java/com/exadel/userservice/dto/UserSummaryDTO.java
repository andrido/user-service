package com.exadel.userservice.dto;

import com.exadel.userservice.model.UserStatus;
import lombok.Data;

@Data
public class UserSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String phone;
    private String status;


}
