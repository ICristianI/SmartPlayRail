package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Repositorio para la entidad JuegoCrucigrama, que representa un juego de crucigrama en la aplicación SmartPlay.
@Repository
public interface JuegoCrucigramaRepository extends JpaRepository<JuegoCrucigrama, Long> {

@Query("SELECT j FROM JuegoCrucigrama j WHERE j.usuario = :usuario ORDER BY j.fechaCreacion DESC")
Page<JuegoCrucigrama> findByUsuario(@Param("usuario") User usuario, Pageable pageable);

}
