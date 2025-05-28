package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoLike;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.JuegoLikeRepository;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Servicio para gestionar los "likes" de los juegos por parte de los usuarios.

@Service
public class JuegoLikeService {

    @Autowired
    private JuegoLikeRepository juegoLikeRepository;

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private UserRepository userRepository;

    // Comprueba si un usuario ha dado "like" a un juego específico.

    public boolean haDadoLike(String email, Long juegoId) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Juego> juego = juegoRepository.findById(juegoId);

        if (user.isEmpty() || juego.isEmpty()) {
            return false;
        }

        return juegoLikeRepository.existsByJuegoAndUsuario(juego.get(),user.get());
    }

    // Alterna el estado de "like" de un juego por parte de un usuario.

    public boolean alternarLike(String email, Long juegoId) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        Optional<Juego> juegoOpt = juegoRepository.findById(juegoId);

        if (userOpt.isEmpty() || juegoOpt.isEmpty()) {
            throw new RuntimeException("Usuario o juego no encontrado.");
        }

        User usuario = userOpt.get();
        Juego juego = juegoOpt.get();

        Optional<JuegoLike> likeExistente = juegoLikeRepository.findByJuegoAndUsuario(juego,usuario);

        if (likeExistente.isPresent()) {
            // Quitar like
            juegoLikeRepository.delete(likeExistente.get());
            juego.setMeGusta(juego.getMeGusta() - 1);
            juegoRepository.save(juego);
            return false; // ya no le gusta
        } else {
            // Dar like
            JuegoLike nuevoLike = new JuegoLike();
            nuevoLike.setUsuario(usuario);
            nuevoLike.setJuego(juego);
            juegoLikeRepository.save(nuevoLike);

            juego.setMeGusta(juego.getMeGusta() + 1);
            juegoRepository.save(juego);
            return true; // ahora le gusta
        }
    }

    
}
