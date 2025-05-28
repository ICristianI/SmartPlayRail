package com.tfg.SmartPlay.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.service.UserComponent;

//Tiene atributos de usuario que permiten gestionar las sesiones, son atributos globales de todas las vistas
//Sirve para mostrar u ocultar elementos de la vista dependiendo de si el usuario está logueado o no

@ControllerAdvice(basePackages = { "com.tfg.SmartPlay.entity", "com.tfg.SmartPlay.controller",
        "com.tfg.SmartPlay.repository", "com.tfg.SmartPlay.security" })
public class UserModelAttributes {

    @Autowired
    private UserComponent userComponent;

    @ModelAttribute("isLogged")
    public boolean isLogged() {
        return userComponent.isLoggedUser();
    }

    @ModelAttribute("isProfesor")
    public boolean isProfesor() {
        return userComponent.isProfesor();
    }

    @ModelAttribute("isAlumno")
    public boolean isAlumno() {
        return userComponent.isAlumno();
    }

    @ModelAttribute("currentUserName")
    public String userName() {
        Optional<User> user = userComponent.getUser();
        if (user.isEmpty()) {
            return "Annoymous";
        } else {
            return user.get().getNombre();
        }
    } 

}
