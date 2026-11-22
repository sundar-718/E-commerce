package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.report.ReportResponse;
import com.ecommerce.admin.entity.Order;
import com.ecommerce.admin.entity.OrderItem;
import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.admin.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final OrderRepository orderRepository;

    @Override
    public ReportResponse getSalesReport(String period) {
        List<Order> orders = orderRepository.findAll();
        Map<String, Long> grouped = orders.stream()
                .filter(order -> matchesPeriod(order.getCreatedAt(), period))
                .collect(Collectors.groupingBy(order -> formatGroupKey(order.getCreatedAt(), period), Collectors.counting()));
        return ReportResponse.builder()
                .period(period)
                .data(grouped.entrySet().stream().map(entry -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("period", entry.getKey());
                    item.put("sales", entry.getValue());
                    return item;
                }).toList())
                .build();
    }

    @Override
    public ReportResponse getRevenueReport(String period) {
        List<Order> orders = orderRepository.findAll();
        Map<String, Double> grouped = orders.stream()
                .filter(order -> matchesPeriod(order.getCreatedAt(), period))
                .collect(Collectors.groupingBy(order -> formatGroupKey(order.getCreatedAt(), period), Collectors.summingDouble(order -> order.getTotalAmount().doubleValue())));
        return ReportResponse.builder()
                .period(period)
                .data(grouped.entrySet().stream().map(entry -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("period", entry.getKey());
                    item.put("revenue", entry.getValue());
                    return item;
                }).toList())
                .build();
    }

    @Override
    public ReportResponse getTopProductsReport(String period) {
        List<Order> orders = orderRepository.findAll();
        Map<String, Long> grouped = orders.stream()
                .filter(order -> matchesPeriod(order.getCreatedAt(), period))
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item.getProduct().getName(), Collectors.summingLong(OrderItem::getQuantity)));
        List<Map<String, Object>> data = new ArrayList<>();
        grouped.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(5).forEach(entry -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("product", entry.getKey());
            item.put("quantity", entry.getValue());
            data.add(item);
        });
        return ReportResponse.builder().period(period).data(data).build();
    }

    @Override
    public ReportResponse getTopCustomersReport(String period) {
        List<Order> orders = orderRepository.findAll();
        Map<String, Double> grouped = orders.stream()
                .filter(order -> matchesPeriod(order.getCreatedAt(), period))
                .collect(Collectors.groupingBy(order -> order.getCustomer().getName(), Collectors.summingDouble(order -> order.getTotalAmount().doubleValue())));
        List<Map<String, Object>> data = new ArrayList<>();
        grouped.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(5).forEach(entry -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("customer", entry.getKey());
            item.put("revenue", entry.getValue());
            data.add(item);
        });
        return ReportResponse.builder().period(period).data(data).build();
    }

    private boolean matchesPeriod(LocalDateTime createdAt, String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase(Locale.ROOT)) {
            case "daily" -> createdAt.toLocalDate().equals(now.toLocalDate());
            case "weekly" -> createdAt.get(WeekFields.of(Locale.US).weekOfYear()) == now.get(WeekFields.of(Locale.US).weekOfYear())
                    && createdAt.getYear() == now.getYear();
            case "monthly" -> createdAt.getMonth() == now.getMonth() && createdAt.getYear() == now.getYear();
            case "yearly" -> createdAt.getYear() == now.getYear();
            default -> true;
        };
    }

    private String formatGroupKey(LocalDateTime createdAt, String period) {
        return switch (period.toLowerCase(Locale.ROOT)) {
            case "daily" -> createdAt.toLocalDate().toString();
            case "weekly" -> createdAt.getYear() + "-W" + createdAt.get(WeekFields.of(Locale.US).weekOfYear());
            case "monthly" -> createdAt.getYear() + "-" + createdAt.getMonthValue();
            case "yearly" -> String.valueOf(createdAt.getYear());
            default -> createdAt.toLocalDate().toString();
        };
    }
}
