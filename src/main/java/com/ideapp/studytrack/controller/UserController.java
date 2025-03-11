package com.ideapp.studytrack.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ideapp.studytrack.model.LoginRequest;
import com.ideapp.studytrack.model.User;
import com.ideapp.studytrack.security.JwtService;
import com.ideapp.studytrack.service.AuthService;
import com.ideapp.studytrack.service.PasswordResetService;
import com.ideapp.studytrack.service.UserService;

@Controller
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
	@ResponseBody // Devuelve una respuesta JSON
	public Map<String, String> resetPassword(@RequestParam("token") String token,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword) {

		Map<String, String> response = new HashMap<>();

		// Validar que las contraseñas coincidan
		if (!newPassword.equals(confirmPassword)) {
			response.put("status", "error");
			response.put("message", "Las contraseñas no coinciden.");
			return response;
		}

		// Procesar el restablecimiento de contraseña
		try {
			passwordResetService.resetPassword(token, newPassword);
			response.put("status", "success");
			response.put("message", "Contraseña restablecida exitosamente.");
			return response;
		} catch (RuntimeException e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
			return response;
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", "Ocurrió un error inesperado. Por favor, intenta nuevamente.");
			return response;
		}
	}

	@GetMapping("/reset-password")
	public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
		// Pasar el token a la vista
		model.addAttribute("token", token);
		return "reset-password"; // Nombre del archivo HTML en `templates`
	}

}