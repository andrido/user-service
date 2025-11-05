package com.exadel.userservice.controller;

import com.exadel.userservice.dto.UserMapper;
import com.exadel.userservice.dto.UserRequestDTO;
import com.exadel.userservice.dto.UserResponseDTO;
import com.exadel.userservice.dto.UserSummaryDTO;
import com.exadel.userservice.model.User;
import com.exadel.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor

public class UserController {
    private final UserMapper userMapper;
    private final UserService service;

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto) {
        User user = userMapper.convertToEntity(dto);
        User saved = service.createUser(user);
        return userMapper.convertToDTO(saved);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }


    @GetMapping
    public List<UserSummaryDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }



}
