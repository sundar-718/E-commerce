package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.report.ReportResponse;

public interface ReportService {
    ReportResponse getSalesReport(String period);

    ReportResponse getRevenueReport(String period);

    ReportResponse getTopProductsReport(String period);

    ReportResponse getTopCustomersReport(String period);
}
