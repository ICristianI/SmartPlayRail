package com.tfg.SmartPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;

// Repositorio para la entidad Ficha, que representa una ficha de información en la aplicación SmartPlay.
@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    @Query("SELECT f FROM Ficha f WHERE f.usuario = :usuario ORDER BY f.fechaCreacion DESC")
    List<Ficha> findByUsuario(@Param("usuario") User usuario);
    
    @Query("SELECT f FROM Ficha f JOIN f.cuadernos c WHERE c.id = :cuadernoId ORDER BY f.fechaCreacion DESC")
    Page<Ficha> obtenerFichasPorCuaderno(@Param("cuadernoId") Long cuadernoId, Pageable pageable);
    
    @Query("SELECT f FROM Ficha f WHERE f.id NOT IN (SELECT cf.id FROM Cuaderno c JOIN c.fichas cf WHERE c.id = :cuadernoId) ORDER BY f.fechaCreacion DESC")
    Page<Ficha> findFichasNoAgregadas(@Param("cuadernoId") Long cuadernoId, Pageable pageable);

    @Query("SELECT f FROM Ficha f WHERE f.usuario = :usuario ORDER BY f.fechaCreacion DESC")
    Page<Ficha> findByUsuario(@Param("usuario") User usuario, Pageable pageable);

    @Query("SELECT f FROM Ficha f WHERE f.privada = false AND LOWER(f.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY f.fechaCreacion DESC")
    Page<Ficha> buscarPublicasPorNombreOrdenFecha(@Param("nombre") String nombre, Pageable pageable);
    
    @Query("SELECT f FROM Ficha f WHERE f.privada = false AND LOWER(f.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY f.meGusta DESC")
    Page<Ficha> buscarPublicasPorNombreOrdenLikes(@Param("nombre") String nombre, Pageable pageable);
    
    @Query("SELECT f FROM Ficha f WHERE f.privada = false ORDER BY f.fechaCreacion DESC")
    Page<Ficha> buscarPublicasOrdenFecha(Pageable pageable);
    
    @Query("SELECT f FROM Ficha f WHERE f.privada = false ORDER BY f.meGusta DESC")
    Page<Ficha> buscarPublicasOrdenLikes(Pageable pageable);
    
}
