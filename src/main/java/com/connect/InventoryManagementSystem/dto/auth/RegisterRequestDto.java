package com.connect.InventoryManagementSystem.dto.auth;

import com.connect.InventoryManagementSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String username;

    private String phoneNumber;

    private String email;

    private String password;

    private Role role;
}
