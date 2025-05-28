package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Grupo;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio para la entidad Grupo, que representa un grupo de usuarios en la aplicación SmartPlay.
@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Devuelve los grupos en los que participa un usuario (paginados).
     */
    @Query("SELECT g FROM Grupo g JOIN g.usuarios u WHERE u = :usuario ORDER BY g.fechaCreacion DESC")
    Page<Grupo> findByUsuariosContaining(@Param("usuario") User usuario, Pageable pageable);

     /**
     * Busca un grupo por su código de acceso único.
     */
    Optional<Grupo> findByCodigoAcceso(String codigoAcceso);

    /**
     * Comprueba si ya existe un grupo con ese código (para evitar duplicados).
     */
    boolean existsByCodigoAcceso(String codigoAcceso);


}
