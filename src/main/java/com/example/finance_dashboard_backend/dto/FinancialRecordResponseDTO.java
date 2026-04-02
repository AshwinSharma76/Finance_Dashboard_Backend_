package com.example.finance_dashboard_backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordResponseDTO {
    private Long id;
    private Double amount;
    private String type;
    private String category;
    private LocalDate date;
    private String description;
}