package com.connect.InventoryManagementSystem.dto.auth;

import com.connect.InventoryManagementSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private String email;
    private Role role;
    private String jwt;
}
