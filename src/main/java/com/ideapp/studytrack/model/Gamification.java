package com.ideapp.studytrack.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Gamification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private User usuario;

	private Integer puntos = 0;
	private Integer nivel = 1;
	private String[] insignias;

	// Getters, Setters, Constructors
}