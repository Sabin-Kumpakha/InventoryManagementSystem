package com.connect.InventoryManagementSystem.service.auth;

import com.connect.InventoryManagementSystem.dto.auth.LoginResponse;
import com.connect.InventoryManagementSystem.dto.auth.RegisterResponse;
import com.connect.InventoryManagementSystem.dto.auth.LoginRequestDto;
import com.connect.InventoryManagementSystem.dto.auth.RegisterRequestDto;
import com.connect.InventoryManagementSystem.exception.UserAlreadyExistsException;
import com.connect.InventoryManagementSystem.repository.UserRepository;
import com.connect.InventoryManagementSystem.security.JwtService;
import com.connect.InventoryManagementSystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse authenticateUser(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword())
            );
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + request.getEmail()));

            return convertToUserDto(user);

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid login credentials", e);
        }
    }

    @Override
    public RegisterResponse registerUser(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email " + request.getEmail() + " is already taken");
        }
        User user = convertToUser(request);
        userRepository.save(user);
        return convertToUserDto2(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    private User convertToUser(RegisterRequestDto request) {
        return new User(
                request.getUsername(),
                request.getPhoneNumber(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );
    }

    private RegisterResponse convertToUserDto2(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private LoginResponse convertToUserDto(User user) {
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .role(user.getRole())
                .jwt(jwtService.generateToken(user))
                .build();
    }

}
