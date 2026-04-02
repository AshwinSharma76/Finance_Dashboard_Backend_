package com.example.finance_dashboard_backend.controller;

import com.example.finance_dashboard_backend.dto.UserRequestDTO;
import com.example.finance_dashboard_backend.dto.UserResponseDTO;
import com.example.finance_dashboard_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // CREATE USER
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        logger.info("Creating user:");
        return ResponseEntity.ok(userService.createUser(dto));
    }

    // GET ALL USERS
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public UserResponseDTO updateStatus(@PathVariable Long id,
                                        @RequestParam String status) {
        logger.info("Updating status of user id {} to {}", id, status);
        return userService.updateUserStatus(id, status);
    }
}