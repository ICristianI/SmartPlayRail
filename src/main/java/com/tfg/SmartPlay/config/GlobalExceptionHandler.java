package com.tfg.SmartPlay.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

// Manejador de excepciones globales
// Se encarga de manejar las excepciones de tamaño de archivo

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("error", "El archivo es demasiado grande. El tamaño máximo permitido es de 5MB.");
        return "redirect:/users/perfil";
    }
}
