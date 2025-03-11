package com.ideapp.studytrack.service;

import java.util.Arrays;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ideapp.studytrack.model.User;
import com.ideapp.studytrack.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// Método para autenticar un usuario
		public User authenticate(String email, String password) {
			// Buscar al usuario por su correo electrónico
			User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

			// Validar la contraseña
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new RuntimeException("Invalid credentials");
			}

			return user;
		}
		//metodo para condicionar seguridad de password
		public void validatePassword(String password) {
	        // Definir las reglas de validación
	        PasswordValidator validator = new PasswordValidator(Arrays.asList(
	            new LengthRule(8, 30), // Longitud mínima de 8 y máxima de 30 caracteres
	            new CharacterRule(EnglishCharacterData.UpperCase, 1), // Al menos una letra mayúscula
	            new CharacterRule(EnglishCharacterData.LowerCase, 1), // Al menos una letra minúscula
	            new CharacterRule(EnglishCharacterData.Digit, 1), // Al menos un número
	            new CharacterRule(EnglishCharacterData.Special, 1), // Al menos un carácter especial
	            new WhitespaceRule() // No se permiten espacios en blanco
	        ));

	        // Validar la contraseña
	        RuleResult result = validator.validate(new PasswordData(password));

	        if (!result.isValid()) {
	            throw new RuntimeException("Password validation failed: " + validator.getMessages(result));
	        }
	    }

}
