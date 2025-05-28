package com.tfg.SmartPlay.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// Controlador de errores

@Controller
public class ErrorControllerSmartPlay implements ErrorController {

    // Cualquier error contemplado tendrá su propia template para avisar al usuario

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error404Template";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error500Template";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error400Template";
            }
        }
        model.addAttribute("num", status.toString());
        return "error";
    }
}