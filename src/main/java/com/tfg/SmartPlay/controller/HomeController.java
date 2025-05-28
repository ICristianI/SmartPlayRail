package com.tfg.SmartPlay.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador para manejar las rutas de la aplicación generales y que no requieren acciones adicionales o específicas.

@Controller
public class HomeController {

    
    // lleva a pagina de inicio

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // lleva a pagina de decision de registro o inicio de sesion

    @GetMapping("/regin")
    public String regin(Model model) {
        return "RegistrarIniciarSesion/RegistrarOIniciar";
    }

    // lleva a pagina de registro

    @GetMapping("/signup")
    public String signup(Model model) {
        return "RegistrarIniciarSesion/Registrar";
    }

    // lleva a pagina de inicio de sesion

    @GetMapping("/login")
    public String signin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "RegistrarIniciarSesion/IniciarSesion";
    }

    // lleva a pagina de inicio de sesion con error

    @GetMapping("/loginerror")
    public String loginerror() {

        return "redirect:/login?error=" + URLEncoder.encode("Contraseña Incorrecta", StandardCharsets.UTF_8);
    }

    // lleva a pagina de inicio de sesion con mensaje de exito

    @GetMapping("/verify")
    public String getMethodName(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "RegistrarIniciarSesion/Verificar";
    }

    // lleva a pagina de configuracion

    @GetMapping("/config")
    public String config(Model model) {
        return "Configuracion/configuration";
    }

    // lleva a pagina de seleccion de fichas o juegos

    @GetMapping("/creacion")
    public String creacion(Model model) {
        return "Creacion";
    }

    // lleva a pagina de fichas

    @GetMapping("/fichas")
    public String fichas(Model model) {
        return "Fichas/Fichas";
    }

    // lleva a pagina de crear fichas

    @GetMapping("/verFichas")
    public String verFichas(Model model) {
        return "Fichas/verFichas";
    }

    // lleva a pagina de crear fichas

    @GetMapping("/Cuadernos")
    public String Cuadernos(Model model) {
        return "Cuadernos/Cuadernos";
    }

    // lleva a pagina de crear cuadernos

    @GetMapping("/crearCuadernos")
    public String crearCuadernos(Model model) {
        return "Cuadernos/crearCuadernos";
    }

    // lleva a pagina de ver juegos

    @GetMapping("/juegos")
    public String juegos(Model model) {
        return "Juegos/Juegos";
    }

    // lleva a pagina de ahorcado

    @GetMapping("/ahorcado")
    public String ahorcado(Model model) {
        return "Juegos/Ahorcado/Ahorcado";
    }

    // lleva a pagina de sopa de letras

    @GetMapping("/sopa")
    public String sopa(Model model) {
        return "Juegos/Sopa/Sopa";
    }

    // lleva a pagina de crucigrama

    @GetMapping("/crucigrama")
    public String crucigrama(Model model) {
        return "Juegos/Crucigrama/Crucigrama";
    }

    // lleva a pagina de grupos

    @GetMapping("/grupo")
    public String grupo(Model model) {
        return "Grupos/Grupos";
    }

    // lleva a pagina de crear grupos

    @GetMapping("/crearGrupos")
    public String crearGrupos(Model model) {
        return "Grupos/crearGrupos";
    }

    // lleva a pagina de ver grupos

    @GetMapping("/verGrupos")
    public String verGrupos(Model model) {
        return "Grupos/verGrupos";
    }


}
