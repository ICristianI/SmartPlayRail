package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.UserComponent;
import com.tfg.SmartPlay.service.UserService;
import com.tfg.SmartPlay.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Controlador para las operaciones relacionadas con los usuarios

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService tokenService;

    // Maneja el formulario de registro con encriptado de contrasreña

    @PostMapping("/register")
    public String register(@ModelAttribute User user, @RequestParam String rol, Model model,
            RedirectAttributes redirectAttributes) {

        if (!userService.validarUsuarioYCorreo(user, model, false)) {
            redirectAttributes.addFlashAttribute("error", "El nombre o correo electrónico ya está en uso.");
            return "redirect:/signup";
        }

        if (rol == null || rol.isEmpty()) {
            user.setRoles(List.of("ALUMNO"));
        } else {
            user.setRoles(List.of(rol));
        }

        Blob photo;
        try {
            photo = imagenService.getDefaultProfileImage();
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener la imagen de perfil predeterminada.");
            return "RegistrarIniciarSesion/Registrar";
        }
        String encode = passwordEncoder.encode(user.getPassword());

        user.setPassword(encode);
        user.setPhoto(photo);
        userRepository.save(user);

        tokenService.sendVerificationEmail(user);

        return "redirect:/verify";

    }

    // Muestra el perfil del usuario

    @GetMapping("/perfil")
    public String profile(Model model) {
        model.addAttribute("user", userComponent.getUser().get());
        return "PaginaUsuario/Perfil";
    }

    // Edita el perfil de un usuario

    @PostMapping("/edit")
    public String editarPerfil(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> usuarioOptional = userRepository.findById(userComponent.getUser().get().getId());

        if (usuarioOptional.isEmpty()) {
            return "redirect:/users/perfil";
        }

        User usuario = usuarioOptional.get();

        // Validar correo y nombre de usuario usando el servicio
        if (!userService.validarUsuarioYCorreo(user, model, true)) {
            redirectAttributes.addFlashAttribute("error", "El nombre ya está en uso.");
            return "redirect:/users/perfil";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente.");
        }

        usuario.setNombre(user.getNombre());
        usuario.setEdad(user.getEdad());

        userRepository.save(usuario);
        return "redirect:/users/perfil";
    }

    // Elimina un usuario

    @PostMapping("/delete")
    public String borrarUsuario(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.deleteUser(userComponent.getUser().get().getId(), request, response);
            model.addAttribute("message", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            model.addAttribute("message", "Error al eliminar el usuario.");
        }
        return "redirect:/";
    }

    // Recupera la imagen de perfil del usuario
    @GetMapping("/image")
    public ResponseEntity<Object> downloadImage() throws SQLException {
        Optional<User> op = userRepository.findById(userComponent.getUser().get().getId());
        ResponseEntity<Object> imageResponse = imagenService.getImageResponse(op.get().getPhoto());
        return imageResponse;
    }

    // Recupera la imagen de perfil de un usuario por su ID
    @GetMapping("images/{id}")
    public ResponseEntity<Object> getMethodName(@PathVariable Long id) throws SQLException {
        Optional<User> op = userRepository.findById(id);
        ResponseEntity<Object> imageResponse = imagenService.getImageResponse(op.get().getPhoto());
        return imageResponse;
    }

    // Sirve para evitar que se intente mapear un multipart a Blob (causa error si se deja sin esto)
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("photo");
    }

    // Guarda la imagen de perfil del usuario controlando el tamaño

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String guardarFotoUser(@RequestParam("photo") MultipartFile photo, Model model,
            RedirectAttributes redirectAttributes) throws Exception {
        // Verificar si la imagen excede el tamaño máximo de 1 MB
        long maxSize = 5 * 1024 * 1024; // 5 MB
        if (photo.getSize() > maxSize) {
            // Si es mayor a 1 MB, mostrar un mensaje de error
            redirectAttributes.addFlashAttribute("error", "La imagen no puede ser mayor a 5 MB.");

            return "redirect:/users/perfil";
        }

        Blob photoBlob = imagenService.saveImage(photo);
        User user = userComponent.getUser().get();
        user.setPhoto(photoBlob);
        userRepository.save(user);

        return "redirect:/users/perfil";
    }

    // Verifica el token de verificación eviado por correo

    @GetMapping("/verificar")
    public String mostrarFormularioVerificacion(
            @RequestParam("token") String token,
            @RequestParam(value = "message", required = false) String message,
            Model model) {

        tokenService.verifyToken(token);

        if (message == null) {
            message = "Verificación exitosa";
        }

        return "redirect:/verify?message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
    }

    // Maneja la excepción de tamaño máximo de archivo

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(@PathVariable Long id, MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("error", "El archivo excede el tamaño máximo permitido.");
        return "redirect:/users/perfil";
    }

    // Permite ver el perfil de otro usuario por su ID
    
    @GetMapping("/perfil/{id}")
    public String verPerfilDeOtroUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> usuarioOptional = userService.findUserById(id);

        if (usuarioOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/"; // O puedes redirigir a una página de error personalizada
        }

        model.addAttribute("user", usuarioOptional.get());
        return "PaginaUsuario/PerfilPublico";
    }

}
