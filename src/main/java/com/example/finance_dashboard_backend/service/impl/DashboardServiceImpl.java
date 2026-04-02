package com.example.finance_dashboard_backend.service.impl;

import com.example.finance_dashboard_backend.dto.DashboardResponseDTO;
import com.example.finance_dashboard_backend.dto.FinancialRecordResponseDTO;
import com.example.finance_dashboard_backend.entity.FinancialRecord;
import com.example.finance_dashboard_backend.repository.FinancialRecordRepository;
import com.example.finance_dashboard_backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private FinancialRecordRepository repository;

    @Override
    public DashboardResponseDTO getSummary() {
        logger.info("Fetching default dashboard summary (5 recent transactions)");
        return getSummary(5); // default recent transactions = 5
    }

    @Override
    public DashboardResponseDTO getSummary(int recentLimit) {
        logger.info("Fetching dashboard summary with recentLimit={}", recentLimit);

        List<FinancialRecord> records = repository.findAllByActiveTrue();

        // Calculate total income
        double totalIncome = records.stream()
                .filter(r -> r.getType().name().equalsIgnoreCase("INCOME"))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        // Calculate total expense
        double totalExpense = records.stream()
                .filter(r -> r.getType().name().equalsIgnoreCase("EXPENSE"))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        double netBalance = totalIncome - totalExpense;

        // Totals per category
        Map<String, Double> categoryTotals = records.stream()
                .collect(Collectors.groupingBy(
                        FinancialRecord::getCategory,
                        Collectors.summingDouble(FinancialRecord::getAmount)
                ));

        // Recent transactions (sorted by date descending)
        List<FinancialRecordResponseDTO> recentTransactions = records.stream()
                .sorted(Comparator.comparing(FinancialRecord::getDate).reversed())
                .limit(recentLimit)
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // Weekly and monthly summaries
        Map<String, Double> weeklyIncome = new HashMap<>();
        Map<String, Double> weeklyExpense = new HashMap<>();
        Map<String, Double> monthlyIncome = new HashMap<>();
        Map<String, Double> monthlyExpense = new HashMap<>();
        WeekFields weekFields = WeekFields.ISO;

        for (FinancialRecord r : records) {
            LocalDate date = r.getDate();
            String weekKey = date.getYear() + "-W" + date.get(weekFields.weekOfWeekBasedYear());
            String monthKey = date.getYear() + "-" + String.format("%02d", date.getMonthValue());

            if (r.getType().name().equalsIgnoreCase("INCOME")) {
                weeklyIncome.put(weekKey, weeklyIncome.getOrDefault(weekKey, 0.0) + r.getAmount());
                monthlyIncome.put(monthKey, monthlyIncome.getOrDefault(monthKey, 0.0) + r.getAmount());
            } else {
                weeklyExpense.put(weekKey, weeklyExpense.getOrDefault(weekKey, 0.0) + r.getAmount());
                monthlyExpense.put(monthKey, monthlyExpense.getOrDefault(monthKey, 0.0) + r.getAmount());
            }
        }

        // Prepare DTO
        DashboardResponseDTO dto = new DashboardResponseDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setNetBalance(netBalance);
        dto.setCategoryTotals(categoryTotals);
        dto.setRecentTransactions(recentTransactions);
        dto.setWeeklyIncome(weeklyIncome);
        dto.setWeeklyExpense(weeklyExpense);
        dto.setMonthlyIncome(monthlyIncome);
        dto.setMonthlyExpense(monthlyExpense);

        logger.info("Dashboard summary prepared successfully");

        return dto;
    }

    @Override
    public FinancialRecordResponseDTO mapToResponse(FinancialRecord record) {
        // Convert entity to DTO
        FinancialRecordResponseDTO dto = new FinancialRecordResponseDTO();
        dto.setId(record.getId());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType().name());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setDescription(record.getDescription());

        logger.debug("Mapped FinancialRecord {} to FinancialRecordResponseDTO", record.getId());
        return dto;
    }
}