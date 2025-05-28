
package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoSopaLetrasRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Blob;
import java.util.Optional;

// Servicio para gestionar los juegos de Sopa de Letras en la aplicación SmartPlay.

@Service
public class JuegoSopaLetrasService {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoSopaLetrasRepository juegoSopaLetrasRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private ImagenService imagenService;

    // Método para guardar un nuevo juego de Sopa de Letras.

   public void guardarJuego(JuegoSopaLetras juego, String email, MultipartFile imagenJuego) {
    User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    juego.setUsuario(usuario);

    try {
        if (imagenJuego != null && !imagenJuego.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenJuego);
            juego.setImagen(imagen);
        } else {
            InputStream defaultImgStream = getClass().getResourceAsStream("/static/images/generalImages/CREARSOPA.png");
            if (defaultImgStream != null) {
                byte[] bytes = defaultImgStream.readAllBytes();
                Blob defaultBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
                juego.setImagen(defaultBlob);
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar la imagen del juego de Sopa de Letras", e);
    }

    juegoSopaLetrasRepository.save(juego);
    }

    // Método para editar un juego de Sopa de Letras existente.

    public void editarJuego(Long juegoId, JuegoSopaLetras juegoEditado, String email) {
        JuegoSopaLetras juego = (JuegoSopaLetras) juegoService.obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setAsignatura(juegoEditado.getAsignatura());
        juego.setContenido(juegoEditado.getContenido());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setPrivada(juegoEditado.isPrivada());
        juego.setPalabras(juegoEditado.getPalabras());

        juegoSopaLetrasRepository.save(juego);
    }

    // Método para obtener todos los cuadernos de Sopa de Letras paginados

    public Page<Cuaderno> obtenerCuadernosConJuegoPaginados(Juego juego, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.obtenerCuadernosPorJuego(juego, pageable);
    }

    // Método para obtener todos los juegos de Sopa de Letras de un usuario paginados.

    public Page<JuegoSopaLetras> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return juegoSopaLetrasRepository.findByUsuario(usuario, pageable);
    }

    // Método para obtener un juego de Sopa de Letras por ID y verificar si el usuario es el propietario.
    
    public Optional<JuegoSopaLetras> obtenerJuego(Long juegoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<JuegoSopaLetras> juego = juegoSopaLetrasRepository.findById(juegoId);
        return juego.filter(j -> j.getUsuario().getId().equals(usuario.getId()));
    }
    
}
