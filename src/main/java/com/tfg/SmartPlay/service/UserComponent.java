package com.tfg.SmartPlay.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Componente para gestionar los usuarios

@Component
public class UserComponent {

    @Autowired
    UserRepository userRepository;

    // Devuelve si el usuario está logueado

    public boolean isLoggedUser() {
        return getUser().isPresent();
    }

    // Cierra la sesión

    public void logout(HttpServletRequest request, HttpServletResponse response) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    // Devuelve el usuario logueado

    public Optional<User> getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username);
    }

    // Devuelve si el usuario es profesor

    public boolean isProfesor() {
        Optional<User> opUser = getUser();
        if (opUser.isEmpty()) {
            return false;
        } else {
            return opUser.get().getRoles().contains("PROFESOR");
        }
    }

    // Devuelve si el usuario es alumno
    
    public boolean isAlumno() {
        Optional<User> opUser = getUser();
        if (opUser.isEmpty()) {
            return false;
        } else {
            return opUser.get().getRoles().contains("ALUMNO");
        }
    }


}
