package com.ideapp.studytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ideapp.studytrack.model.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
	
}
