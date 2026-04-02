package com.example.finance_dashboard_backend.service;

import com.example.finance_dashboard_backend.dto.FinancialRecordRequestDTO;
import com.example.finance_dashboard_backend.dto.FinancialRecordResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordService {

    FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO dto);

    List<FinancialRecordResponseDTO> getAllRecords(); // old method

    Page<FinancialRecordResponseDTO> getAllRecords(int page, int size, String sortField, String sortDirection);

    FinancialRecordResponseDTO updateRecord(Long id, FinancialRecordRequestDTO dto);

    void deleteRecord(Long id); // optional, can keep for hard delete

    void softDeleteRecord(Long id); // new soft delete

    List<FinancialRecordResponseDTO> filterByCategory(String category);

    List<FinancialRecordResponseDTO> filterByType(String type);

    List<FinancialRecordResponseDTO> filterByDate(LocalDate start, LocalDate end);

    List<FinancialRecordResponseDTO> filterRecords(String category, String type, LocalDate start, LocalDate end);
}