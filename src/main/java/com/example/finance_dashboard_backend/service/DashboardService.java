package com.example.finance_dashboard_backend.service;

import com.example.finance_dashboard_backend.dto.DashboardResponseDTO;
import com.example.finance_dashboard_backend.dto.FinancialRecordResponseDTO;
import com.example.finance_dashboard_backend.entity.FinancialRecord;

public interface DashboardService {
    DashboardResponseDTO getSummary();
    FinancialRecordResponseDTO mapToResponse(FinancialRecord record);
     DashboardResponseDTO getSummary(int recentLimit);
}
