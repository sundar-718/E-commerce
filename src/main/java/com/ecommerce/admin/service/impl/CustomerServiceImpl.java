package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.customer.CustomerResponse;
import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.CustomerStatus;
import com.ecommerce.admin.exception.ResourceNotFoundException;
import com.ecommerce.admin.mapper.CustomerMapper;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Override
    public PageResponse<CustomerResponse> getCustomers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Customer> customerPage;
        if (search == null || search.isBlank()) {
            customerPage = customerRepository.findAll(pageable);
        } else {
            customerPage = customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        }
        return toPageResponse(customerPage);
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse disableCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customer.setStatus(CustomerStatus.DISABLED);
        log.info("Customer disabled: {}", customer.getEmail());
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponse enableCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customer.setStatus(CustomerStatus.ACTIVE);
        log.info("Customer enabled: {}", customer.getEmail());
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }

    private PageResponse<CustomerResponse> toPageResponse(Page<Customer> page) {
        return PageResponse.<CustomerResponse>builder()
                .content(page.getContent().stream().map(CustomerMapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
