package com.example.finance_dashboard_backend.repository;

import com.example.finance_dashboard_backend.entity.FinancialRecord;
import com.example.finance_dashboard_backend.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // Find records by category (all matching records, active status handled in service)
    List<FinancialRecord> findByCategory(String category);

    // Find records by type (INCOME/EXPENSE)
    List<FinancialRecord> findByType(TransactionType type);

    // Find records within a specific date range
    List<FinancialRecord> findByDateBetween(LocalDate start, LocalDate end);

    // Get all records marked as active (soft delete)
    List<FinancialRecord> findAllByActiveTrue();

    // Paginated retrieval of active records
    Page<FinancialRecord> findAllByActiveTrue(Pageable pageable);

    /**
     * Advanced filtering for dashboard or search functionality.
     * Each parameter is optional; null values are ignored.
     *
     * @param category filter by category if not null
     * @param type filter by transaction type (INCOME/EXPENSE) if not null
     * @param start filter records after or on this date if not null
     * @param end filter records before or on this date if not null
     * @return list of filtered FinancialRecord entities
     */
    @Query("SELECT r FROM FinancialRecord r WHERE r.active = true " +
            "AND (:category IS NULL OR r.category = :category) " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:start IS NULL OR r.date >= :start) " +
            "AND (:end IS NULL OR r.date <= :end)")
    List<FinancialRecord> filterRecords(
            @Param("category") String category,
            @Param("type") TransactionType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}