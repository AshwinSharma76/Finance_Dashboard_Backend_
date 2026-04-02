package com.example.finance_dashboard_backend.controller;

import com.example.finance_dashboard_backend.dto.FinancialRecordRequestDTO;
import com.example.finance_dashboard_backend.dto.FinancialRecordResponseDTO;
import com.example.finance_dashboard_backend.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {

    private static final Logger logger = LoggerFactory.getLogger(FinancialRecordController.class);

    @Autowired
    private FinancialRecordService service;

    // CREATE RECORD
    @PostMapping
    public FinancialRecordResponseDTO create(@RequestBody FinancialRecordRequestDTO dto) {
        logger.info("Creating new financial record: {}", dto);
        return service.createRecord(dto);
    }

    // GET ALL RECORDS
    @GetMapping
    public List<FinancialRecordResponseDTO> getAll() {
        logger.info("Fetching all financial records");
        return service.getAllRecords();
    }

    // UPDATE RECORD
    @PutMapping("/{id}")
    public FinancialRecordResponseDTO update(@PathVariable Long id,
                                             @RequestBody FinancialRecordRequestDTO dto) {
        logger.info("Updating financial record with id {}: {}", id, dto);
        return service.updateRecord(id, dto);
    }

    // DELETE RECORD
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        logger.info("Deleting financial record with id {}", id);
        service.deleteRecord(id);
        return "Record deleted successfully";
    }

    // FILTER BY CATEGORY
    @GetMapping("/filter/category")
    public List<FinancialRecordResponseDTO> byCategory(@RequestParam String category) {
        logger.info("Filtering financial records by category: {}", category);
        return service.filterByCategory(category);
    }

    // FILTER BY TYPE
    @GetMapping("/filter/type")
    public List<FinancialRecordResponseDTO> byType(@RequestParam String type) {
        logger.info("Filtering financial records by type: {}", type);
        return service.filterByType(type);
    }

    // FILTER BY DATE RANGE
    @GetMapping("/filter/date")
    public List<FinancialRecordResponseDTO> byDate(@RequestParam String start,
                                                   @RequestParam String end) {
        logger.info("Filtering financial records from {} to {}", start, end);
        return service.filterByDate(LocalDate.parse(start), LocalDate.parse(end));
    }

    // COMBINED FILTER
    @GetMapping("/filter")
    public List<FinancialRecordResponseDTO> filter(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end
    ) {
        LocalDate startDate = start != null ? LocalDate.parse(start) : null;
        LocalDate endDate = end != null ? LocalDate.parse(end) : null;

        logger.info("Filtering financial records with category={}, type={}, start={}, end={}",
                category, type, startDate, endDate);

        return service.filterRecords(category, type, startDate, endDate);
    }
}