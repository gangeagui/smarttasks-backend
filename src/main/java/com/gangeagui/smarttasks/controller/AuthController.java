package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.dto.AuthRequest;
import com.gangeagui.smarttasks.dto.AuthResponse;
import com.gangeagui.smarttasks.exception.ResourceNotFoundException;
import com.gangeagui.smarttasks.model.User;
import com.gangeagui.smarttasks.repository.UserRepository;
import com.gangeagui.smarttasks.security.JwtService;
import com.gangeagui.smarttasks.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        passwordResetService.sendPasswordResetToken(email);
        return ResponseEntity.ok("Correo de recuperación enviado");
    }
}
