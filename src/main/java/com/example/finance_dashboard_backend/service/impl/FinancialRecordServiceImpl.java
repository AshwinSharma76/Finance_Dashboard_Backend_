package com.example.finance_dashboard_backend.service.impl;

import com.example.finance_dashboard_backend.dto.FinancialRecordRequestDTO;
import com.example.finance_dashboard_backend.dto.FinancialRecordResponseDTO;
import com.example.finance_dashboard_backend.entity.FinancialRecord;
import com.example.finance_dashboard_backend.enums.TransactionType;
import com.example.finance_dashboard_backend.exception.BadRequestException;
import com.example.finance_dashboard_backend.exception.ResourceNotFoundException;
import com.example.finance_dashboard_backend.repository.FinancialRecordRepository;
import com.example.finance_dashboard_backend.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private static final Logger logger = LoggerFactory.getLogger(FinancialRecordServiceImpl.class);

    @Autowired
    private FinancialRecordRepository repository;

    @Override
    public FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO dto) {
        logger.info("Creating financial record for userId={}", dto.getCreatedBy());

        FinancialRecord record = new FinancialRecord();
        record.setAmount(dto.getAmount());
        record.setType(TransactionType.valueOf(dto.getType().toUpperCase()));
        record.setCategory(dto.getCategory());
        record.setDate(dto.getDate());
        record.setDescription(dto.getDescription());
        record.setCreatedBy(dto.getCreatedBy());
        record.setActive(true); // ensure record is active

        FinancialRecord saved = repository.save(record);
        logger.info("Financial record created successfully with id={}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public List<FinancialRecordResponseDTO> getAllRecords() {
        logger.info("Fetching all active financial records");
        return repository.findAllByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FinancialRecordResponseDTO> getAllRecords(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching paginated records page={} size={} sortField={} sortDirection={}", page, size, sortField, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() :
                Sort.by(sortField).ascending();

        return repository.findAllByActiveTrue(PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
    }

    @Override
    public FinancialRecordResponseDTO updateRecord(Long id, FinancialRecordRequestDTO dto) {
        logger.info("Updating financial record with id={}", id);

        FinancialRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        record.setAmount(dto.getAmount());
        try {
            record.setType(TransactionType.valueOf(dto.getType().toUpperCase()));
        } catch (Exception e) {
            throw new BadRequestException("Invalid transaction type: " + dto.getType());
        }
        record.setCategory(dto.getCategory());
        record.setDate(dto.getDate());
        record.setDescription(dto.getDescription());

        FinancialRecord saved = repository.save(record);
        logger.info("Financial record updated successfully with id={}", saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public void deleteRecord(Long id) {
        logger.info("Deleting financial record with id={}", id);
        repository.deleteById(id); // optional hard delete
    }

    @Override
    public void softDeleteRecord(Long id) {
        logger.info("Soft deleting financial record with id={}", id);

        FinancialRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        record.setActive(false);
        repository.save(record);
    }

    private FinancialRecordResponseDTO mapToResponse(FinancialRecord record) {
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

    @Override
    public List<FinancialRecordResponseDTO> filterByCategory(String category) {
        logger.info("Filtering records by category={}", category);
        return repository.findByCategory(category)
                .stream()
                .filter(FinancialRecord::getActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FinancialRecordResponseDTO> filterByType(String type) {
        logger.info("Filtering records by type={}", type);
        return repository.findByType(TransactionType.valueOf(type.toUpperCase()))
                .stream()
                .filter(FinancialRecord::getActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FinancialRecordResponseDTO> filterByDate(LocalDate start, LocalDate end) {
        logger.info("Filtering records by date range {} to {}", start, end);
        return repository.findByDateBetween(start, end)
                .stream()
                .filter(FinancialRecord::getActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FinancialRecordResponseDTO> filterRecords(String category, String type, LocalDate start, LocalDate end) {
        logger.info("Filtering records with category={}, type={}, start={}, end={}", category, type, start, end);

        TransactionType txType = null;
        if (type != null && !type.isEmpty()) {
            try {
                txType = TransactionType.valueOf(type.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid transaction type: " + type);
            }
        }

        return repository.filterRecords(
                        category != null && !category.isEmpty() ? category : null,
                        txType,
                        start,
                        end
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}