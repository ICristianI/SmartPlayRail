package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Grupo;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.repository.GrupoRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

// Servicio para gestionar los grupos de usuarios en la aplicación SmartPlay.

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CuadernoRepository cuadernoRepository;

    /**
     * Lista todos los grupos de un usuario autenticado, paginados.
     */
    public Page<Grupo> listarGruposPaginados(String email, int page, int size) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pageable pageable = PageRequest.of(page, size);
        return grupoRepository.findByUsuariosContaining(usuario, pageable);
    }

    /**
     * Guarda un nuevo grupo creado por un usuario.
     */
    public Grupo guardarGrupo(Grupo grupo, String email, List<Long> cuadernosId) {
        User creador = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Cuaderno> caudernosSeleccionados = cuadernoRepository.findAllById(cuadernosId);
        if (caudernosSeleccionados != null) {
            grupo.setCuadernos(caudernosSeleccionados);
        }
        grupo.setCreador(creador);
        grupo.setUsuarios(new ArrayList<>(List.of(creador)));
        grupo.setCodigoAcceso(generarCodigoUnico());

        return grupoRepository.save(grupo);
    }

    /**
     * Permite a un usuario unirse a un grupo por código.
     */
    public boolean unirseAGrupo(String codigo, String email) {
        Optional<Grupo> grupoOpt = grupoRepository.findByCodigoAcceso(codigo);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (grupoOpt.isEmpty() || userOpt.isEmpty()) return false;

        Grupo grupo = grupoOpt.get();
        User usuario = userOpt.get();

        if (grupo.getUsuarios().contains(usuario)) return false;
        if (grupo.getUsuarios().size() >= 100) return false;

        grupo.getUsuarios().add(usuario);
        grupoRepository.save(grupo);
        return true;
    }

    /**
     * Devuelve el grupo si el usuario tiene acceso.
     */
    public Grupo obtenerGrupo(Long grupoId, String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);

        if (grupoOpt.isPresent() && grupoOpt.get().getUsuarios().contains(usuario)) {
            return grupoOpt.get();
        }

        return null;
    }

    /**
     * Edita un grupo si el usuario autenticado es el creador.
     */
    public boolean editarGrupo(Long grupoId, String nuevoNombre, String nuevaDescripcion, List<Long> nuevosCuadernosIds, String email) {
        Optional<User> usuarioOpt = userRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) return false;
    
        User usuario = usuarioOpt.get();
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
    
        if (grupoOpt.isPresent() && grupoOpt.get().getCreador().getId().equals(usuario.getId())) {
            Grupo grupo = grupoOpt.get();
    
            // Actualizar nombre si no está vacío
            if (!nuevoNombre.isBlank()) {
                grupo.setNombre(nuevoNombre.trim());
            }
    
            // Actualizar descripción (puede estar vacía)
            grupo.setDescripcion(nuevaDescripcion != null ? nuevaDescripcion.trim() : "");
    
            // Añadir nuevos cuadernos si se han enviado
            if (nuevosCuadernosIds != null && !nuevosCuadernosIds.isEmpty()) {
                List<Cuaderno> nuevosCuadernos = cuadernoRepository.findAllById(nuevosCuadernosIds);
                for (Cuaderno cuaderno : nuevosCuadernos) {
                    if (!grupo.getCuadernos().contains(cuaderno)) {
                        grupo.getCuadernos().add(cuaderno);
                    }
                }
            }
    
            grupoRepository.save(grupo);
            return true;
        }
    
        return false;
    }
    
    

    /**
     * Elimina un grupo si el usuario autenticado es el creador.
     */
    public boolean eliminarGrupo(Long grupoId, String email) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (grupoOpt.isEmpty() || userOpt.isEmpty()) return false;

        Grupo grupo = grupoOpt.get();
        User usuario = userOpt.get();

        if (!grupo.getCreador().getId().equals(usuario.getId())) return false;

        grupoRepository.deleteById(grupoId);
        return true;
    }

    /**
     * Genera un código alfanumérico único de 8 caracteres.
     */
    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (grupoRepository.existsByCodigoAcceso(codigo));
        return codigo;
    }

    /**
     * Devuelve los cuadernos paginados de un grupo.
     */
    public Page<Cuaderno> obtenerCuadernosPaginados(Long grupoId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cuadernoRepository.findByGrupoId(grupoId, pageable);
    }
    
    public List<Cuaderno> obtenerCuadernosNoAgregados(Grupo grupo, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return List.of();
    
        User usuario = userOpt.get();
        List<Cuaderno> cuadernosGrupo = grupo.getCuadernos();
    
        if (cuadernosGrupo == null || cuadernosGrupo.isEmpty()) {
            return cuadernoRepository.findByUsuario(usuario);
        } else {
            return cuadernoRepository.findByUsuarioAndNotInGrupo(usuario, cuadernosGrupo);
        }
    }

    /**
     * Elimina un cuaderno del grupo si el usuario autenticado es el creador del grupo.
     */
    public boolean eliminarCuadernoDelGrupo(Long grupoId, Long cuadernoId, String emailUsuario) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
    
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            if (!grupo.getCreador().getEmail().equals(emailUsuario)) {
                return false;
            }
    
            grupo.getCuadernos().removeIf(c -> c.getId().equals(cuadernoId));
            grupoRepository.save(grupo);
            return true;
        }
    
        return false;
    }

    /**
     * Obtiene los usuarios de un grupo paginados.
     */
    public Page<User> obtenerUsuariosPaginados(Long grupoId, int page, int size) {
        Grupo grupo = grupoRepository.findById(grupoId).orElse(null);
        if (grupo == null) return Page.empty();
        List<User> usuarios = grupo.getUsuarios();
        int start = page * size;
        int end = Math.min(start + size, usuarios.size());
        if (start >= usuarios.size()) return Page.empty();
        return new PageImpl<>(usuarios.subList(start, end), PageRequest.of(page, size), usuarios.size());
    }

    /**
     * Verifica si el usuario autenticado es el propietario del grupo.
     */
    public boolean isPropietario(Grupo grupo, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;
    
        User usuario = userOpt.get();
        return grupo.getCreador().getId().equals(usuario.getId());
    }
    

    /**
     * Elimina un usuario del grupo si el propietario lo solicita.
     */
    public boolean eliminarUsuarioDelGrupo(Long grupoId, Long usuarioId, String emailPropietario) {
        Optional<Grupo> grupoOpt = grupoRepository.findById(grupoId);
        Optional<User> usuarioEliminarOpt = userRepository.findById(usuarioId);
        Optional<User> propietarioOpt = userRepository.findByEmail(emailPropietario);
    
        if (grupoOpt.isEmpty() || usuarioEliminarOpt.isEmpty() || propietarioOpt.isEmpty()) return false;
    
        Grupo grupo = grupoOpt.get();
        User usuarioAEliminar = usuarioEliminarOpt.get();
        User propietario = propietarioOpt.get();
    
        if (!grupo.getCreador().getId().equals(propietario.getId()) ||
            grupo.getCreador().getId().equals(usuarioAEliminar.getId())) {
            return false;
        }
    
        boolean eliminado = grupo.getUsuarios().removeIf(u -> u.getId().equals(usuarioAEliminar.getId()));
        if (eliminado) {
            grupoRepository.save(grupo);
        }
    
        return eliminado;
    }
    
    

}
