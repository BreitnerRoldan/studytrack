package com.ideapp.studytrack.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ideapp.studytrack.model.User;
import com.ideapp.studytrack.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthService authService;

	// Lista de extensiones de dominio permitidas
	private static final List<String> ALLOWED_DOMAINS = Arrays.asList("com","gov", "org", "net", "edu", "co", "io");

	// Método para registrar un nuevo usuario
	public User registerUser(User user) {

		// Validar el formato del correo electrónico
		if (!isValidEmail(user.getEmail())) {
			throw new RuntimeException("Invalid email format");
		}

		// Validar que el correo electrónico no esté duplicado
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		
		// Validar la contraseña
		authService.validatePassword(user.getPassword());

		// Encriptar la contraseña antes de guardarla
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Guardar el usuario en la base de datos
		return userRepository.save(user);
	}

	// Método privado para validar el formato del correo electrónico
	private boolean isValidEmail(String email) {
	    // Eliminar espacios al inicio y al final
	    email = email.trim();
	    // Primera validación: Formato básico usando Apache Commons Validator
	    if (!EmailValidator.getInstance().isValid(email)) {
	    	System.out.print("EMAIL:" + email);	        
	    	throw new RuntimeException("Invalid email format");
	    }
	    // Segunda validación: Restringir extensiones de dominio
	    String domain = email.substring(email.lastIndexOf('.') + 1).toLowerCase();
	    if (!ALLOWED_DOMAINS.contains(domain)) {
	        throw new RuntimeException("Domain not allowed: " + domain);
	    }
	    return true;
	}

}