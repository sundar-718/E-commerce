package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.order.OrderResponse;
import com.ecommerce.admin.dto.order.OrderStatusUpdateRequest;
import com.ecommerce.admin.entity.Order;
import com.ecommerce.admin.entity.OrderStatus;
import com.ecommerce.admin.exception.ResourceNotFoundException;
import com.ecommerce.admin.mapper.OrderMapper;
import com.ecommerce.admin.repository.OrderRepository;
import com.ecommerce.admin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    @Override
    public PageResponse<OrderResponse> getOrders(int page, int size, String sortBy, String direction, String search, String status) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orderPage;
        if (status != null && !status.isBlank()) {
            orderPage = orderRepository.findByOrderStatus(OrderStatus.valueOf(status.toUpperCase()), pageable);
        } else if (search != null && !search.isBlank()) {
            List<Order> matchingOrders = orderRepository.findAll().stream()
                    .filter(order -> order.getOrderNumber().contains(search) || order.getCustomer().getName().contains(search))
                    .toList();
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), matchingOrders.size());
            List<Order> pagedOrders = start < matchingOrders.size() ? matchingOrders.subList(start, end) : List.of();
            orderPage = new PageImpl<>(pagedOrders, pageable, matchingOrders.size());
        } else {
            orderPage = orderRepository.findAll(pageable);
        }
        return toPageResponse(orderPage);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        order.setOrderStatus(request.getOrderStatus());
        log.info("Order status updated for order {} to {}", order.getOrderNumber(), request.getOrderStatus());
        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public PageResponse<OrderResponse> getOrdersByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByOrderStatus(OrderStatus.valueOf(status.toUpperCase()), pageable);
        return toPageResponse(orderPage);
    }

    private PageResponse<OrderResponse> toPageResponse(Page<Order> page) {
        return PageResponse.<OrderResponse>builder()
                .content(page.getContent().stream().map(OrderMapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
