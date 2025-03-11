package com.ideapp.studytrack.repository;

import com.ideapp.studytrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar un usuario por su correo electrónico
    Optional<User> findByEmail(String email);

    // Verificar si un correo electrónico ya existe
    boolean existsByEmail(String email);
}