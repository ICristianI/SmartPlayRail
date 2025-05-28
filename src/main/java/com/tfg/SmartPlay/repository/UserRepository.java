package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositorio para la entidad User, que representa a un usuario en la aplicación SmartPlay.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRoles(String role);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNombre(String nombre);

    Optional<User> findByNombre(String nombre);
}
