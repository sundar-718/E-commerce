package com.ecommerce.admin.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryResponse {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private LocalDateTime createdAt;
}
