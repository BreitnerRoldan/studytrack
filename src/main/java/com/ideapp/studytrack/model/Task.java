package com.ideapp.studytrack.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "materia_id", nullable = false)
    private Subject materia;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    private String prioridad; // ALTA, MEDIA, BAJA
    private LocalDate fechaLimite;
    private Integer progreso = 0;
    private Boolean completada = false;

    // Getters, Setters, Constructors
}