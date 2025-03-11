package com.ideapp.studytrack.repository;

import com.ideapp.studytrack.model.PasswordResetToken;
import com.ideapp.studytrack.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Método para buscar un token por su valor
    Optional<PasswordResetToken> findByToken(String token);
    PasswordResetToken findByUser(User user);
}