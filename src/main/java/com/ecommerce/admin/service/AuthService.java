package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.auth.AuthRequest;
import com.ecommerce.admin.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
