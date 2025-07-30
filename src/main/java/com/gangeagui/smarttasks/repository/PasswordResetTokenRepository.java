package com.gangeagui.smarttasks.repository;

import com.gangeagui.smarttasks.model.PasswordResetToken;
import com.gangeagui.smarttasks.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user);
}
