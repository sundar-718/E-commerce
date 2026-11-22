package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.order.OrderResponse;
import com.ecommerce.admin.dto.order.OrderStatusUpdateRequest;

public interface OrderService {
    PageResponse<OrderResponse> getOrders(int page, int size, String sortBy, String direction, String search, String status);

    OrderResponse getOrder(Long id);

    OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request);

    PageResponse<OrderResponse> getOrdersByStatus(String status, int page, int size);
}
