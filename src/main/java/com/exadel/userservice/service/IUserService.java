package com.exadel.userservice.service;

import com.exadel.userservice.dto.UserResponseDTO;
import com.exadel.userservice.dto.UserSummaryDTO;
import com.exadel.userservice.model.User;
import java.util.List;

public interface IUserService {
    UserResponseDTO getUserById(Long id);
    List<UserSummaryDTO> getAllUsers();
    User createUser(User user);
    void deleteUser(Long id);
    User updateUser(User user);

}
