package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.dto.EmailRequest;
import com.gangeagui.smarttasks.dto.PasswordResetDto;
import com.gangeagui.smarttasks.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest request) {
        passwordResetService.sendResetToken(request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Correo de recuperación enviado.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto resetDto) {
        passwordResetService.resetPassword(resetDto.getToken(), resetDto.getNewPassword());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña restablecida correctamente.");

        return ResponseEntity.ok(response);
    }
}
