package com.tfg.SmartPlay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Repositorio para la entidad Cuaderno, se usan consultas personalizadas para obtener cuadernos según diferentes criterios.
@Repository
public interface CuadernoRepository extends JpaRepository<Cuaderno, Long> {

    @Query("SELECT c FROM Cuaderno c WHERE c.usuario.email = :email ORDER BY c.fechaCreacion DESC")
    Page<Cuaderno> findByUsuarioEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT c FROM Cuaderno c JOIN c.fichas f WHERE f = :ficha ORDER BY c.fechaCreacion DESC")
    Page<Cuaderno> findByFichasContaining(@Param("ficha") Ficha ficha, Pageable pageable);
    
    @Query("SELECT c FROM Cuaderno c JOIN c.juegos j WHERE j = :juego ORDER BY c.fechaCreacion DESC")
    Page<Cuaderno> obtenerCuadernosPorJuego(@Param("juego") Juego juego, Pageable pageable);
    
    @Query("SELECT c FROM Cuaderno c WHERE c.usuario = :usuario AND c NOT IN :cuadernosGrupo ORDER BY c.fechaCreacion DESC")
    List<Cuaderno> findByUsuarioAndNotInGrupo(@Param("usuario") User usuario, @Param("cuadernosGrupo") List<Cuaderno> cuadernosGrupo);
    
    @Query("SELECT c FROM Cuaderno c WHERE c.usuario = :usuario ORDER BY c.fechaCreacion DESC")
    List<Cuaderno> findByUsuario(@Param("usuario") User usuario);
    
    @Query("SELECT c FROM Cuaderno c JOIN c.fichas f WHERE f = :ficha ORDER BY c.fechaCreacion DESC")
    List<Cuaderno> findByFichasContaining(@Param("ficha") Ficha ficha);
    
    @Query("SELECT c FROM Cuaderno c JOIN c.grupos g WHERE g.id = :grupoId ORDER BY c.fechaCreacion DESC")
    Page<Cuaderno> findByGrupoId(@Param("grupoId") Long grupoId, Pageable pageable);

}
