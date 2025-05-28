package com.tfg.SmartPlay.service;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

// Servicio para gestionar los usuarios

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private VerificationTokenService tokenService;

    // Valida que el usuario y el correo no estén en uso

    public boolean validarUsuarioYCorreo(User user, Model model, boolean isEditing) {
        Optional<User> usuarioExistente = userRepository.findByEmail(user.getEmail());
        Optional<User> nombreExistente = userRepository.findByNombre(user.getNombre());
    
        if (usuarioExistente.isPresent()) {
            
            if (!isEditing || user.getId() == null || !usuarioExistente.get().getId().equals(user.getId())) {
                model.addAttribute("error", "El correo electrónico ya está en uso.");
                return false;
            }
        }
    
        if (nombreExistente.isPresent()) {
            if (!isEditing || user.getId() == null || !nombreExistente.get().getId().equals(user.getId())) {
                model.addAttribute("error", "El nombre de usuario ya está en uso.");
                return false;
            }
        }
    
        return true;
    }
    

    // Elimina un usuario

    public void deleteUser(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            userComponent.logout(request, response);
        } else {
            throw new Exception("Usuario no encontrado.");
        }
    }

    // Para registrar al usuario hace falta que verifique su correo

    public void registerUser(User user) {
        user.setEnabled(false);
        userRepository.save(user);

        tokenService.sendVerificationEmail(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
}
