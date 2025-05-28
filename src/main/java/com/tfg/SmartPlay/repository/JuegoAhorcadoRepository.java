package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Repositorio para la entidad JuegoAhorcado, que representa un juego de ahorcado en la aplicación SmartPlay.
@Repository
public interface JuegoAhorcadoRepository extends JpaRepository<JuegoAhorcado, Long> {

    @Query("SELECT j FROM JuegoAhorcado j WHERE j.usuario = :usuario ORDER BY j.fechaCreacion DESC")
    Page<JuegoAhorcado> findByUsuario(@Param("usuario") User usuario, Pageable pageable);
}

