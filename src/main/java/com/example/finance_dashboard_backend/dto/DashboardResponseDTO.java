package com.example.finance_dashboard_backend.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardResponseDTO {
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;

    private Map<String, Double> categoryTotals;

    private List<FinancialRecordResponseDTO> recentTransactions;

    // Trends
    private Map<String, Double> weeklyIncome;   // key = Week (e.g., 2026-W14)
    private Map<String, Double> weeklyExpense;

    private Map<String, Double> monthlyIncome;  // key = Month (e.g., 2026-04)
    private Map<String, Double> monthlyExpense;
}