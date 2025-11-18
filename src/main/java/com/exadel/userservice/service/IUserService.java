package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserRequestDTO;
import com.exadel.userservice.dto.UserResponseDTO;
import com.exadel.userservice.dto.UserSummaryDTO;
import com.exadel.userservice.model.User;
import java.util.List;

public interface IUserService {
    UserResponseDTO getUserById(Long id);
    List<UserSummaryDTO> getAllUsers();
    UserResponseDTO createUser(UserRequestDTO user);
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO user);

}
