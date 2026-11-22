package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.customer.CustomerResponse;

public interface CustomerService {
    PageResponse<CustomerResponse> getCustomers(int page, int size, String search);

    CustomerResponse getCustomer(Long id);

    CustomerResponse disableCustomer(Long id);

    CustomerResponse enableCustomer(Long id);

    void deleteCustomer(Long id);
}
