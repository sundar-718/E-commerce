package com.ecommerce.admin.service.impl;

import com.ecommerce.admin.dto.auth.AuthRequest;
import com.ecommerce.admin.dto.auth.AuthResponse;
import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.User;
import com.ecommerce.admin.exception.UnauthorizedException;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.repository.UserRepository;
import com.ecommerce.admin.security.JwtTokenProvider;
import com.ecommerce.admin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            String email = principal.getUsername();
            User user = userRepository.findByEmail(email).orElse(null);
            String role = user != null ? user.getRole() : "ROLE_CUSTOMER";
            if (user == null) {
                Customer customer = customerRepository.findByEmail(email)
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
                role = customer.getRole();
            }
            String token = jwtTokenProvider.generateToken(email, role);
            return AuthResponse.builder()
                    .token(token)
                    .email(email)
                    .role(role)
                    .build();
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }
}
