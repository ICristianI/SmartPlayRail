package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaLike;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaLikeRepository;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Servicio para manejar los "me gusta" de las fichas en la aplicación SmartPlay.
@Service
@RequiredArgsConstructor
public class FichaLikeService {

    private final FichaRepository fichaRepository;
    private final UserRepository userRepository;
    private final FichaLikeRepository fichaLikeRepository;

    // Método para alternar el estado de "me gusta" de una ficha por parte de un usuario.

    public void alternarLike(String email, Long fichaId) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Ficha ficha = fichaRepository.findById(fichaId)
                .orElseThrow(() -> new RuntimeException("Ficha no encontrada"));

        Optional<FichaLike> like = fichaLikeRepository.findByFichaAndUsuario(ficha, usuario);

        if (like.isPresent()) {
            fichaLikeRepository.delete(like.get());
            ficha.setMeGusta(ficha.getMeGusta() - 1);
        } else {
            fichaLikeRepository.save(new FichaLike(null, usuario, ficha));
            ficha.setMeGusta(ficha.getMeGusta() + 1);
        }

        fichaRepository.save(ficha);
    }

    // Método para ver si se ha dado "me gusta" a una ficha.
    public boolean haDadoLike(String email, Long fichaId) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Ficha> ficha = fichaRepository.findById(fichaId);

        return user.isPresent() && ficha.isPresent() &&
                fichaLikeRepository.existsByFichaAndUsuario(ficha.get(), user.get());
    }
}

