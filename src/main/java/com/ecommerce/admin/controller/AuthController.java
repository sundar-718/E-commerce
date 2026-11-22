package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.auth.AuthRequest;
import com.ecommerce.admin.dto.auth.AuthResponse;
import com.ecommerce.admin.dto.customer.CustomerProfileResponse;
import com.ecommerce.admin.dto.customer.CustomerRegistrationRequest;
import com.ecommerce.admin.service.AuthService;
import com.ecommerce.admin.service.CustomerModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CustomerModuleService customerModuleService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerProfileResponse> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.status(201).body(customerModuleService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
