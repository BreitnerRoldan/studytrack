package com.ideapp.studytrack.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ideapp.studytrack.Iservice.ICuentaService;
import com.ideapp.studytrack.repository.CuentaRepository;

@Service
public class CuentaService implements ICuentaService {

	private final CuentaRepository cuentaRepository;

	public CuentaService(CuentaRepository cuentaRepository) {
		this.cuentaRepository = cuentaRepository;
	}
	
	
	@Override
	public List<String> obtenerTodosLosNumerosCuenta() {
        List<String> numeros = new ArrayList<>();
        numeros.add("1234567890");
        numeros.add("0987654321");
        numeros.add("1122334455");
        return numeros;
    }

	//public List<String> obtenerTodosLosNumerosCuenta() {
		//return cuentaRepository.findAll().stream().map(Cuenta::getNumeroCuenta).toList();
	//}

}
