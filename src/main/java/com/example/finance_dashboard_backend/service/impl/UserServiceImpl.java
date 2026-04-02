package com.example.finance_dashboard_backend.service.impl;

import com.example.finance_dashboard_backend.dto.UserRequestDTO;
import com.example.finance_dashboard_backend.dto.UserResponseDTO;
import com.example.finance_dashboard_backend.entity.User;
import com.example.finance_dashboard_backend.enums.Role;
import com.example.finance_dashboard_backend.enums.Status;
import com.example.finance_dashboard_backend.exception.BadRequestException;
import com.example.finance_dashboard_backend.exception.ResourceNotFoundException;
import com.example.finance_dashboard_backend.repository.UserRepository;
import com.example.finance_dashboard_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        logger.info("Creating new user with email={}", dto.getEmail());

        // Check duplicate email
        if (userRepository.existsByEmail(dto.getEmail())) {
            logger.error("Email already exists: {}", dto.getEmail());
            throw new BadRequestException("Email already exists");
        }

        // Validate role safely
        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (Exception e) {
            logger.error("Invalid role provided: {}", dto.getRole());
            throw new BadRequestException("Invalid role: " + dto.getRole());
        }

        //  Create user entity
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);
        user.setStatus(Status.ACTIVE);

        //  Save user
        User saved = userRepository.save(user);
        logger.info("User created successfully with id={}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserStatus(Long userId, String status) {
        logger.info("Updating status of user id={} to {}", userId, status);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setStatus(Status.valueOf(status));

        User saved = userRepository.save(user);
        logger.info("User status updated successfully for id={}", saved.getId());

        return mapToResponse(saved);
    }

    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.getStatus().name());

        logger.debug("Mapped User {} to UserResponseDTO", user.getId());
        return dto;
    }
}