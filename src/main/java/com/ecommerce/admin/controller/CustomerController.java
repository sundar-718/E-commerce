package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.PageResponse;
import com.ecommerce.admin.dto.customer.CustomerResponse;
import com.ecommerce.admin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(customerService.getCustomers(page, size, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<CustomerResponse> disableCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.disableCustomer(id));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<CustomerResponse> enableCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.enableCustomer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
