package com.ideapp.studytrack.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ideapp.studytrack.model.LoginRequest;
import com.ideapp.studytrack.model.User;
import com.ideapp.studytrack.security.JwtService;
import com.ideapp.studytrack.service.AuthService;
import com.ideapp.studytrack.service.PasswordResetService;
import com.ideapp.studytrack.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordResetService passwordResetService;

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	// Endpoint para registrar un nuevo usuario
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.ok("User registered successfully: " + registeredUser.getEmail());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error during registration: " + e.getMessage());
		}
	}

	// Endpoint para iniciar sesión y generar un token JWT
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			// Autenticar al usuario
			User user = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

			// Generar un token JWT
			String token = jwtService.generateToken(user.getEmail());

			// Devolver el token como respuesta
			return ResponseEntity.ok(Map.of("token", token));
		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
		}
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		passwordResetService.requestPasswordReset(email);
		return ResponseEntity.ok("Password reset email sent");
	}

	@PostMapping("/reset-password")
	@ResponseBody
	public Map<String, String> resetPassword(@RequestBody Map<String, String> requestBody) {

		// Obtener el token del cuerpo de la solicitud
		String token = requestBody.get("token");

		// Validar que el token no sea nulo ni vacío
		if (token == null || token.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", "El token es requerido.");
			return response;
		}

		// Obtener las contraseñas del cuerpo de la solicitud
		String newPassword = requestBody.get("newPassword");
		String confirmPassword = requestBody.get("confirmPassword");

		// Validar que las contraseñas coincidan y no estén vacías
		if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", "Las contraseñas no coinciden o están vacías.");
			return response;
		}

		// Validar la contraseña usando Passay
		List<String> validationErrors = validatePassword(newPassword);
		if (!validationErrors.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", String.join(", ", validationErrors)); // Devuelve todos los mensajes de error
			return response;
		}

		try {
			// Procesar el restablecimiento de contraseña
			passwordResetService.resetPassword(token, newPassword);

			// Respuesta exitosa
			Map<String, String> response = new HashMap<>();
			response.put("status", "success");
			response.put("message", "Contraseña restablecida exitosamente.");
			return response;

		} catch (RuntimeException e) {
			// Capturar errores específicos lanzados por el servicio
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", e.getMessage());
			return response;
		} catch (Exception e) {
			// Capturar errores inesperados
			Map<String, String> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", "Ocurrió un error inesperado. Por favor, intenta nuevamente.");
			return response;
		}
	}

	// Método para validar la contraseña usando Passay
	private List<String> validatePassword(String password) {
		// Definir las reglas para la contraseña
		PasswordValidator validator = new PasswordValidator(List.of(new LengthRule(8, 50), // Longitud mínima y máxima
				new CharacterRule(EnglishCharacterData.Digit, 1), // Al menos un número
				new CharacterRule(EnglishCharacterData.UpperCase, 1), // Al menos una letra mayúscula
				new CharacterRule(EnglishCharacterData.LowerCase, 1), // Al menos una letra minúscula
				new CharacterRule(EnglishCharacterData.Special, 1), // Al menos un carácter especial
				new WhitespaceRule() // No permitir espacios en blanco
		));

		// Validar la contraseña
		RuleResult result = validator.validate(new PasswordData(password));

		// Si hay errores, devolver los mensajes correspondientes
		if (!result.isValid()) {
			return validator.getMessages(result);
		}

		// Si no hay errores, devolver una lista vacía
		return List.of();
	}

	@GetMapping("/reset-password")
	public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
		// Pasar el token a la vista
		model.addAttribute("token", token);
		return "reset-password"; // Nombre del archivo HTML en `templates`
	}

}