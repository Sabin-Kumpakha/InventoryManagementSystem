package com.connect.InventoryManagementSystem;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "12345";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
    }
}