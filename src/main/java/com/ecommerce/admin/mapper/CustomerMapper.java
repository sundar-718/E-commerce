package com.ecommerce.admin.mapper;

import com.ecommerce.admin.dto.customer.CustomerResponse;
import com.ecommerce.admin.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .status(customer.getStatus())
                .role(customer.getRole())
                .build();
    }
}
