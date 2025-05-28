package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.JuegoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Servicio para gestionar los juegos en la aplicación SmartPlay.

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

    /**
     * Obtiene todos los juegos de un usuario sin paginación.
     */
    public List<Juego> obtenerTodosLosJuegosPorUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene todos los juegos de un usuario paginados.
     */
    public Page<Juego> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Pageable pageable = PageRequest.of(page, size);
        return juegoRepository.obtenerJuegosPaginados(usuario, pageable);
    }

    /**
     * Obtiene un juego por ID y verifica si el usuario es el propietario.
     */
    public Optional<Juego> obtenerJuego(Long juegoId, String email) {
        return juegoRepository.findByIdAndUsuario_Email(juegoId, email);
    }

    /**
     * Guarda un nuevo juego para un usuario.
     */
    public void guardarJuego(Juego juego, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        juego.setUsuario(usuario);
        juegoRepository.save(juego);
    }

    /**
     * Edita un juego si pertenece al usuario autenticado.
     */
    public void editarJuego(Long juegoId, Juego juegoEditado, String email) {
        Juego juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setComentarios(juegoEditado.getComentarios());
        juego.setPrivada(juegoEditado.isPrivada());

        juegoRepository.save(juego);
    }

    /**
     * Elimina un juego si pertenece al usuario autenticado.
     */

    public void eliminarJuego(Long juegoId, String email) {
        Juego juego = obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        for (var cuaderno : juego.getCuadernos()) {
            cuaderno.getJuegos().remove(juego);
            cuaderno.setNumeroJuegos(Math.max(0, cuaderno.getNumeroJuegos() - 1));
        }

        juego.getCuadernos().clear();
        juegoRepository.save(juego);

        juegoRepository.delete(juego);
    }

    /**
     * Obtiene juegos no agregados a un cuaderno.
     */
    public List<Juego> obtenerJuegosNoAgregados(Long cuadernoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return juegoRepository.findJuegosNoAgregados(cuadernoId, usuario);
    }

    /**
     * Obtiene juegos pertenecientes a un cuaderno de forma paginada.
     */
    public Page<Juego> obtenerJuegosPaginadosEnCuaderno(Long cuadernoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return juegoRepository.obtenerJuegosPorCuaderno(cuadernoId, pageable);
    }

    /**
     * Obtiene un juego por su ID.
     */
    public Optional<Juego> obtenerJuegoPorId(Long id) {
        return juegoRepository.findById(id);
    }

    /**
     * Obtiene todos los juegos públicos paginados.
     */
    public Page<Juego> obtenerTodosLosJuegos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return juegoRepository.findAll(pageable);
    }

    /**
     * Obtiene la imagen de un juego en formato Blob y la devuelve como ResponseEntity.
     */
    public ResponseEntity<Object> obtenerImagenJuego(Blob imagenBlob) throws SQLException {
        return imagenService.getImageResponse(imagenBlob);
    }

    /**
     * Obtiene juegos públicos ordenados por fecha, paginados.
     */
    public Page<Juego> ordenarPorFecha(String buscar, int page, int size) {
        if (buscar != null && !buscar.trim().isEmpty()) {
            return juegoRepository.buscarPublicosPorNombreFecha(buscar,
                    PageRequest.of(page, size));
        }
        return juegoRepository.buscarPublicosPorFecha(PageRequest.of(page, size));
    }

    /**
     * Obtiene juegos públicos ordenados por número de "me gusta", paginados.
     */
    public Page<Juego> ordenarPorMeGusta(String buscar, int page, int size) {
        if (buscar != null && !buscar.trim().isEmpty()) {
            return juegoRepository.buscarPublicosPorNombreLikes(buscar,
                    PageRequest.of(page, size));
        }
        return juegoRepository.buscarPublicosPorLikes(PageRequest.of(page, size));
    }

    /**
     * Obtiene juegosaccesibles que no estén en privado.
     */
    public Optional<Juego> obtenerJuegoAccesible(Long juegoId, String email) {
        Optional<Juego> juegoOpt = juegoRepository.findById(juegoId);

        return juegoOpt.filter(j -> !j.isPrivada() || j.getUsuario().getEmail().equals(email));
    }

    /**
     * Obtiene un ahorcado accesible por su ID y el email del usuario.
     */
    public Optional<JuegoAhorcado> obtenerAhorcadoAccesible(Long juegoId, String email) {
        return obtenerJuegoAccesible(juegoId, email)
                .filter(j -> j instanceof JuegoAhorcado)
                .map(j -> (JuegoAhorcado) j);
    }


    /**
     * Obtiene un crucigrama accesible por su ID y el email del usuario.
     */
    public Optional<JuegoCrucigrama> obtenerCrucigramaAccesible(Long juegoId, String email) {
        return obtenerJuegoAccesible(juegoId, email)
                .filter(j -> j instanceof JuegoCrucigrama)
                .map(j -> (JuegoCrucigrama) j);
    }

    /**
     * Obtiene una sopa de letras accesible por su ID y el email del usuario.
     */
    public Optional<JuegoSopaLetras> obtenerSopaLetrasAccesible(Long juegoId, String email) {
        return obtenerJuegoAccesible(juegoId, email)
                .filter(j -> j instanceof JuegoSopaLetras)
                .map(j -> (JuegoSopaLetras) j);
    }

}
