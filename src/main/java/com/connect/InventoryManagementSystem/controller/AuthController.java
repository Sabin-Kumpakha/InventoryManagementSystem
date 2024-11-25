package com.connect.InventoryManagementSystem.controller;

import com.connect.InventoryManagementSystem.dto.auth.LoginRequestDto;
import com.connect.InventoryManagementSystem.dto.auth.RegisterRequestDto;
import com.connect.InventoryManagementSystem.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto request) {
        Map<String, Object> response = new HashMap<>();
        var data = authService.authenticateUser(request);
        try {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Login successful");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequestDto request) {
        Map<String, Object> response = new HashMap<>();
        var data = authService.registerUser(request);
        try {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Registration successful");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    //test
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Test");
    }
}
