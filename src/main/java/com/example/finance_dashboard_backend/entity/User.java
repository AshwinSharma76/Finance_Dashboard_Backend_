package com.example.finance_dashboard_backend.entity;

import com.example.finance_dashboard_backend.enums.Role;
import com.example.finance_dashboard_backend.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated

    private String name; // Full name of the user

    @Column(unique = true)
    private String email; // Unique email for login

    private String password; // Hashed password for authentication

    @Enumerated(EnumType.STRING)
    private Role role; // User role (e.g., ADMIN, USER) stored as string in DB

    @Enumerated(EnumType.STRING)
    private Status status; // User status (e.g., ACTIVE, INACTIVE) stored as string
}