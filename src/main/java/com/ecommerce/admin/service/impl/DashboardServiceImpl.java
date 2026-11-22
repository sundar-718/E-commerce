package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.dashboard.DashboardResponse;
import com.ecommerce.admin.entity.OrderStatus;
import com.ecommerce.admin.repository.CategoryRepository;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.admin.repository.ProductRepository;
import com.ecommerce.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardResponse getDashboard() {
        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        long totalCustomers = customerRepository.count();
        long totalOrders = orderRepository.count();
        long pendingOrders = orderRepository.findAll().stream().filter(order -> order.getOrderStatus() == OrderStatus.PENDING).count();
        long deliveredOrders = orderRepository.findAll().stream().filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED).count();
        double totalRevenue = orderRepository.findAll().stream()
                .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
        long lowStockProducts = productRepository.findByStockLessThanEqual(5).size();

        return DashboardResponse.builder()
                .totalProducts(totalProducts)
                .totalCategories(totalCategories)
                .totalCustomers(totalCustomers)
                .totalOrders(totalOrders)
                .pendingOrders(pendingOrders)
                .deliveredOrders(deliveredOrders)
                .totalRevenue(totalRevenue)
                .lowStockProducts(lowStockProducts)
                .build();
    }
}
