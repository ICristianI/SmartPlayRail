package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Repositorio para la entidad JuegoSopaLetras, que representa un juego de sopa de letras en la aplicación SmartPlay.
@Repository
public interface JuegoSopaLetrasRepository extends JpaRepository<JuegoSopaLetras, Long> {

    // Buscar juegos por usuario con paginación
@Query("SELECT j FROM JuegoSopaLetras j WHERE j.usuario = :usuario ORDER BY j.fechaCreacion DESC")
Page<JuegoSopaLetras> findByUsuario(@Param("usuario") User usuario, Pageable pageable);

}
