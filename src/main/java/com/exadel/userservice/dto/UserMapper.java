package com.exadel.userservice.dto;

import com.exadel.userservice.model.User;
import com.exadel.userservice.model.UserRole;
import com.exadel.userservice.model.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserMapper {


    public User convertToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }


    public UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setBorrowedBookTitles(Collections.emptyList()); // Lista vazia
        return dto;
    }



    public UserResponseDTO convertToDTO(User user, List<String> borrowedBookTitles) {
        UserResponseDTO dto = convertToDTO(user); // reaproveita campos comuns
        dto.setBorrowedBookTitles(borrowedBookTitles); // adiciona títulos
        return dto;
    }
    public List<UserSummaryDTO> convertToSummaryDTOList(List<User> users) {
        return users.stream()
                .map(user -> {
                    UserSummaryDTO dto = new UserSummaryDTO();
                    dto.setId(user.getId());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setEmail(user.getEmail());
                    dto.setPhone(user.getPhone());
                    dto.setRole(user.getRole().name());
                    dto.setStatus(user.getStatus().name());
                    return dto;
                })
                .toList();
    }



}
