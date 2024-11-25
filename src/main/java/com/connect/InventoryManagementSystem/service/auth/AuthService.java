package com.connect.InventoryManagementSystem.service.auth;

import com.connect.InventoryManagementSystem.dto.auth.LoginResponse;
import com.connect.InventoryManagementSystem.dto.auth.RegisterResponse;
import com.connect.InventoryManagementSystem.dto.auth.LoginRequestDto;
import com.connect.InventoryManagementSystem.dto.auth.RegisterRequestDto;
import com.connect.InventoryManagementSystem.model.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    LoginResponse authenticateUser(LoginRequestDto request);

    RegisterResponse registerUser(RegisterRequestDto request);

    User getUserByEmail(String email);
}
