package com.ecommerce.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {
    private long totalProducts;
    private long totalCategories;
    private long totalCustomers;
    private long totalOrders;
    private long pendingOrders;
    private long deliveredOrders;
    private double totalRevenue;
    private long lowStockProducts;
}
