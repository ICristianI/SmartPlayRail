package com.tfg.SmartPlay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaUsuario;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.FichaUsuarioRepository;


// Servicio para gestionar las notas y respuestas de los usuarios en fichas específicas.

@Service
public class FichaUsuarioService {

    @Autowired
    private FichaUsuarioRepository fichaUsuarioRepository;

    @Autowired
    private FichaRepository fichaRepository;


    // Método para guardar o actualizar la nota de un usuario en una ficha específica.
    
    public void guardarNota(Long fichaId, Optional<User> user, Double nota) {
        Ficha ficha = fichaRepository.findById(fichaId).orElseThrow();
        FichaUsuario fichaUsuario = fichaUsuarioRepository.findByFichaAndUsuario(ficha, user)
                .orElseGet(() -> {
                    FichaUsuario nuevo = new FichaUsuario();
                    nuevo.setFicha(ficha);
                    nuevo.setUsuario(user.orElse(null));
                    return nuevo;
                });

        fichaUsuario.setNota(nota);

        fichaUsuarioRepository.save(fichaUsuario);
    }

    public Optional<FichaUsuario> obtenerNota(Long fichaId, Long userId) {
        return fichaUsuarioRepository.findByFichaIdAndUsuarioId(fichaId, userId);
    }
}
