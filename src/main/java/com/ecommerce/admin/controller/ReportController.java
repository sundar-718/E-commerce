package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.report.ReportResponse;
import com.ecommerce.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<ReportResponse> getSalesReport(@RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(reportService.getSalesReport(period));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ReportResponse> getRevenueReport(@RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(reportService.getRevenueReport(period));
    }

    @GetMapping("/top-products")
    public ResponseEntity<ReportResponse> getTopProducts(@RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(reportService.getTopProductsReport(period));
    }

    @GetMapping("/top-customers")
    public ResponseEntity<ReportResponse> getTopCustomers(@RequestParam(defaultValue = "monthly") String period) {
        return ResponseEntity.ok(reportService.getTopCustomersReport(period));
    }
}
