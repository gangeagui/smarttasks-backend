package com.gangeagui.smarttasks.service;

import com.gangeagui.smarttasks.model.PasswordResetToken;
import com.gangeagui.smarttasks.model.User;
import com.gangeagui.smarttasks.repository.PasswordResetTokenRepository;
import com.gangeagui.smarttasks.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final long EXPIRATION_MINUTES = 30;

    @Transactional
    public void sendPasswordResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con el email: " + email);
        }

        User user = optionalUser.get();

        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        PasswordResetToken resetToken = new PasswordResetToken(token, expiration, user);
        tokenRepository.save(resetToken);

        String link = "http://localhost:5173/reset-password?token=" + token;

        String message = "Haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" + link;

        emailService.sendEmail(email, "Recuperación de contraseña", message);
    }

    public boolean isValidToken(String token) {
        Optional<PasswordResetToken> result = tokenRepository.findByToken(token);
        return result.isPresent() && result.get().getExpiration().isAfter(LocalDateTime.now());
    }

    public String getEmailByToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> t.getUser().getEmail())
                .orElse(null);
    }

    public void invalidateToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }
}
