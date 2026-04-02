package com.example.finance_dashboard_backend.service;

import com.example.finance_dashboard_backend.dto.UserRequestDTO;
import com.example.finance_dashboard_backend.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUserStatus(Long userId, String status);

}