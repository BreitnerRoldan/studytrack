package com.ideapp.studytrack.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideapp.studytrack.Iservice.ICuentaService;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

	private final ICuentaService cuentaService;

    public CuentaController(ICuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

	@GetMapping("/numerosCuenta")
	public ResponseEntity<List<String>> obtenerTodosLosNumerosCuenta() {

		List<String> cuentaNumero = cuentaService.obtenerTodosLosNumerosCuenta();

		if (cuentaNumero.isEmpty()) {
			return ResponseEntity.noContent().build();// HTTP 204 No Content
		} else {
			return ResponseEntity.ok(cuentaNumero);// HTTP 200 OK con cuerpo
		}

	}

}
