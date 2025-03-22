package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.user.UserAuthDto;
import org.example.dto.user.UserGoogleAuthDto;
import org.example.dto.user.UserRegisterDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Реєстрація нового користувача
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok(Map.of("message", "User '" + userDto.getUsername() + "' registered successfully!"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Registration error: " + e.getMessage()));
        }
    }

    // Логін користувача та отримання JWT токену
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthDto userDto) {
        try {
            var accessToken = userService.authenticateUser(userDto);
            return ResponseEntity.ok("Bearer " + accessToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login error: " + e.getMessage());
        }
    }

    @PostMapping("/google")
    public ResponseEntity<String> googleLogin(@RequestBody UserGoogleAuthDto userDto) {
        try {
            // Перевірка, чи існує користувач і чи правильні дані
            String token = userService.signInGoogle(userDto.getToken());
            return ResponseEntity.ok("Bearer " + token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Google login error: " + e.getMessage());
        }
    }
}
