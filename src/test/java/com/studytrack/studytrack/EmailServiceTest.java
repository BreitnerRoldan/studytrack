package com.studytrack.studytrack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import com.ideapp.studytrack.UserServiceApplication;

@SpringBootTest
@ContextConfiguration(classes = UserServiceApplication.class)
class EmailServiceTest {
	
	@Configuration
    static class TestConfig {
        // Configuración personalizada para las pruebas
    }

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void testSendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("juan@example.com");
            message.setSubject("Test Email");
            message.setText("This is a test email.");
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}