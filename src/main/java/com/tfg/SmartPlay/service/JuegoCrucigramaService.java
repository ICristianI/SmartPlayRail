package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.JuegoCrucigramaRepository;
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

// Servicio para gestionar los juegos de crucigrama en la aplicación SmartPlay.

@Service
public class JuegoCrucigramaService {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoCrucigramaRepository juegoCrucigramaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private ImagenService imagenService;

    // Método para guardar un nuevo juego de crucigrama.

    public void guardarJuego(JuegoCrucigrama juego, String email, MultipartFile imagenJuego) {
    User usuario = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    juego.setUsuario(usuario);
    juego.setPistas(juego.getPistas());
    juego.setRespuestas(juego.getRespuestas());

    try {
        if (imagenJuego != null && !imagenJuego.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenJuego);
            juego.setImagen(imagen);
        } else {
            InputStream defaultImgStream = getClass().getResourceAsStream("/static/images/generalImages/CREARCRUCIGRAMA.png");
            if (defaultImgStream != null) {
                byte[] bytes = defaultImgStream.readAllBytes();
                Blob defaultBlob = new javax.sql.rowset.serial.SerialBlob(bytes);
                juego.setImagen(defaultBlob);
            }
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar la imagen del juego de Crucigrama", e);
    }

    juegoCrucigramaRepository.save(juego);
}

    // Método para editar un juego de crucigrama existente.

    public void editarJuego(Long juegoId, JuegoCrucigrama juegoEditado, String email) {
        JuegoCrucigrama juego = (JuegoCrucigrama) juegoService.obtenerJuego(juegoId, email)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado o sin permisos"));

        juego.setNombre(juegoEditado.getNombre());
        juego.setIdioma(juegoEditado.getIdioma());
        juego.setAsignatura(juegoEditado.getAsignatura());
        juego.setContenido(juegoEditado.getContenido());
        juego.setDescripcion(juegoEditado.getDescripcion());
        juego.setPrivada(juegoEditado.isPrivada());
        juego.setPistas(juegoEditado.getPistas());
        juego.setRespuestas(juegoEditado.getRespuestas());
        juegoCrucigramaRepository.save(juego);
    }


    // Método para obtener todos los cuadernos de crucigrama de un usuario.

    public Page<Cuaderno> obtenerCuadernosConJuegoPaginados(JuegoCrucigrama juego, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.obtenerCuadernosPorJuego(juego, pageable);
    }

    // Método para obtener todos los juegos de crucigrama de un usuario, paginados.

    public Page<JuegoCrucigrama> obtenerJuegosPaginadosPorUsuario(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return juegoCrucigramaRepository.findByUsuario(usuario, pageable);
    }

    // Método para obtener un juego de crucigrama por su ID y el email del usuario.
    
    public Optional<JuegoCrucigrama> obtenerJuego(Long juegoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Optional<JuegoCrucigrama> juego = juegoCrucigramaRepository.findById(juegoId);
        return juego.filter(j -> j.getUsuario().getId().equals(usuario.getId()));
    }
    
}
