package com.example.finance_dashboard_backend.controller;

import com.example.finance_dashboard_backend.dto.DashboardResponseDTO;
import com.example.finance_dashboard_backend.service.impl.DashboardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardServiceImpl service;

    /**
     * Get dashboard summary
     * @param recent Number of recent records to include (default 5)
     * @return DashboardResponseDTO containing summary info
     */
    @GetMapping("/summary")
    public DashboardResponseDTO getSummary(@RequestParam(defaultValue = "5") int recent) {
        logger.info("Fetching dashboard summary for {} recent records", recent);
        return service.getSummary(recent);
    }
}