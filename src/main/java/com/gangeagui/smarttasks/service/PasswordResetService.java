package com.gangeagui.smarttasks.service;

import com.gangeagui.smarttasks.exception.ResourceNotFoundException;
import com.gangeagui.smarttasks.model.PasswordResetToken;
import com.gangeagui.smarttasks.model.User;
import com.gangeagui.smarttasks.repository.PasswordResetTokenRepository;
import com.gangeagui.smarttasks.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void sendResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún usuario con ese correo."));

        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiration(expiration);
        tokenRepository.save(resetToken);

        String fullUrl = resetPasswordUrl + "?token=" + token;

        String subject = "Restablece tu contraseña";
        String content = "Hola " + user.getName() + ",\n\n"
                + "Haz clic en el siguiente enlace para restablecer tu contraseña:\n"
                + fullUrl + "\n\n"
                + "Este enlace expirará en 30 minutos.\n\n"
                + "Si no solicitaste esto, puedes ignorar este mensaje.";

        emailService.sendEmail(user.getEmail(), subject, content);
    }

    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            throw new IllegalArgumentException("Token inválido.");
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El token ha expirado.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}
