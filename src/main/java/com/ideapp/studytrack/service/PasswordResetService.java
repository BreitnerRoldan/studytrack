package com.ideapp.studytrack.service;

import com.ideapp.studytrack.model.PasswordResetToken;
import com.ideapp.studytrack.model.User;
import com.ideapp.studytrack.repository.PasswordResetTokenRepository;
import com.ideapp.studytrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
	private PasswordEncoder passwordEncoder;

    // Método para solicitar restablecimiento de contraseña
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
     // Buscar si ya existe un token activo para el usuario
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);

        if (existingToken != null) {
            // Eliminar el token existente si aún no ha expirado
            passwordResetTokenRepository.delete(existingToken);
        }

        // Generar un token único
        String token = UUID.randomUUID().toString();

        // Guardar el token en la base de datos
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        passwordResetTokenRepository.save(resetToken);

        // Enviar el correo con el enlace de restablecimiento
        //registrar dominio en un servicio como Google Domains , GoDaddy , o cualquier otro registrador de dominios
        String resetLink = "http://localhost:8080/api/users/reset-password?token=" + token;
        sendResetEmail(user.getEmail(), resetLink);
    }

    // Método para enviar el correo de restablecimiento
    private void sendResetEmail(String email, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText("Hello,\n\n" +
                    "You have requested to reset your password. Please click the link below to proceed:\n\n" +
                    resetLink + "\n\n" +
                    "If you did not request this, please ignore this email.\n\n" +
                    "Thank you!");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    // Método para restablecer la contraseña
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }
   
        // Actualizar la contraseña del usuario
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Eliminar el token después de usarlo
        passwordResetTokenRepository.delete(resetToken);
    }
}