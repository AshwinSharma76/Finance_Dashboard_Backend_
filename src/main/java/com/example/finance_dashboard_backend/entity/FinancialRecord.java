package com.example.finance_dashboard_backend.entity;

import com.example.finance_dashboard_backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "financial_records")
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated

    private Double amount; // Amount of the transaction

    @Enumerated(EnumType.STRING)
    private TransactionType type; // Type of transaction (INCOME, EXPENSE) stored as string

    private String category; // Category of transaction (e.g., Salary, Grocery)

    private LocalDate date; // Date of the transaction

    private String description; // Optional description or notes

    private Long createdBy; // ID of the user who created this record

    private Boolean active = true; // Soft delete flag; true means the record is active
}