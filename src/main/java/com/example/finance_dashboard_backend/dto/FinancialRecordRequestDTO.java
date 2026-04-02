package com.example.finance_dashboard_backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordRequestDTO {
    private Double amount;
    private String type;
    private String category;
    private LocalDate date;
    private String description;
    private Long createdBy;
}