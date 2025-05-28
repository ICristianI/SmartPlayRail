package com.tfg.SmartPlay.repository;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoLike;
import com.tfg.SmartPlay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repositorio para la entidad JuegoLike, que representa los "me gusta" de un juego por parte de un usuario en la aplicación SmartPlay.
public interface JuegoLikeRepository extends JpaRepository<JuegoLike, Long> {

    Optional<JuegoLike> findByJuegoAndUsuario(Juego juego, User usuario);

    boolean existsByJuegoAndUsuario(Juego juego, User usuario);

    long countByJuego(Juego juego);

    void deleteByJuegoAndUsuario(Juego juego, User usuario);
    
    void deleteByJuego_Id(Long juegoId);

}
