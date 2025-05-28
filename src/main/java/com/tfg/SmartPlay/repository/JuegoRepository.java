package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

// Repositorio para la entidad Juego, que representa un juego en la aplicación SmartPlay.
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario ORDER BY j.fechaCreacion DESC")
    List<Juego> findByUsuario(@Param("usuario") User usuario);

    Optional<Juego> findByIdAndUsuario_Email(Long id, String email);

    @Query("SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId ORDER BY j.fechaCreacion DESC")
    Page<Juego> obtenerJuegosPorCuaderno(@Param("cuadernoId") Long cuadernoId, Pageable pageable);

    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario AND j NOT IN (SELECT j FROM Juego j JOIN j.cuadernos c WHERE c.id = :cuadernoId) ORDER BY j.fechaCreacion DESC")
    List<Juego> findJuegosNoAgregados(@Param("cuadernoId") Long cuadernoId, @Param("usuario") User usuario);

    @Query("SELECT j FROM Juego j WHERE j.usuario = :usuario ORDER BY j.fechaCreacion DESC")
    Page<Juego> obtenerJuegosPaginados(@Param("usuario") User usuario, Pageable pageable);

    // Buscar públicos por nombre (ignorando mayúsculas/minúsculas), ordenados por
    // fecha
    @Query("SELECT j FROM Juego j WHERE j.privada = false AND LOWER(j.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY j.fechaCreacion DESC")
    Page<Juego> buscarPublicosPorNombreFecha(@Param("nombre") String nombre, Pageable pageable);

    // Buscar públicos por nombre (ignorando mayúsculas/minúsculas), ordenados por
    // me gusta
    @Query("SELECT j FROM Juego j WHERE j.privada = false AND LOWER(j.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY j.meGusta DESC")
    Page<Juego> buscarPublicosPorNombreLikes(@Param("nombre") String nombre, Pageable pageable);

    // Buscar todos los públicos ordenados por fecha
    @Query("SELECT j FROM Juego j WHERE j.privada = false ORDER BY j.fechaCreacion DESC")
    Page<Juego> buscarPublicosPorFecha(Pageable pageable);

    // Buscar todos los públicos ordenados por me gusta
    @Query("SELECT j FROM Juego j WHERE j.privada = false ORDER BY j.meGusta DESC")
    Page<Juego> buscarPublicosPorLikes(Pageable pageable);

}
