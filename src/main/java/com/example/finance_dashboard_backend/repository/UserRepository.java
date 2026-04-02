package com.example.finance_dashboard_backend.repository;

import com.example.finance_dashboard_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Check if a user exists by email. Used for preventing duplicate registrations.
     *
     * @param email email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}