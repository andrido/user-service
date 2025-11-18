package com.exadel.userservice.dto;

import com.exadel.userservice.model.UserRole;
import com.exadel.userservice.model.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private List<String> borrowedBookTitles;
}
