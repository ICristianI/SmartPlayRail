package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;

import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.service.JuegoLikeService;
import com.tfg.SmartPlay.service.JuegoService;
import com.tfg.SmartPlay.service.JuegoSopaLetrasService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Controlador del juego de sopa de letras

@Controller
@RequestMapping("/sopaletras")
public class JuegoSopaLetrasController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoSopaLetrasService juegoSopaLetrasService;

    @Autowired
    private JuegoLikeService juegoLikeService;

    /*
     * Listar juegos de sopa de letras del usuario autenticado con paginación.
     */
    @GetMapping("/listar")
    public String listarJuegos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<JuegoSopaLetras> juegosPage = juegoSopaLetrasService
                .obtenerJuegosPaginadosPorUsuario(userDetails.getUsername(), page, size);

        boolean pages = juegosPage.getTotalPages() > 0;

        List<Map<String, Object>> juegosProcesados = juegosPage.getContent().stream().map(juego -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", juego.getId());
            map.put("nombre", juego.getNombre());
            map.put("asignatura", juego.getAsignatura());
            map.put("meGusta", juego.getMeGusta());
            map.put("fechaFormateada", juego.getFechaCreacionFormateada());
            return map;
        }).toList();

        model.addAttribute("juegos", juegosProcesados);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", juegosPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < juegosPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < juegosPage.getTotalPages() - 1 ? page + 1 : page);

        return "/Juegos/Sopa/verJuegosSopaLetras";
    }

    /*
     * Permite al usuario jugar a un juego de sopa de letras.
     * Verifica si el juego es accesible para el usuario autenticado.
     */
    @GetMapping("/jugar")
    public String jugarSopaLetras(Model model, HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long juegoId = (Long) session.getAttribute("juegoId");

        String email = (userDetails != null) ? userDetails.getUsername() : null;
        Optional<JuegoSopaLetras> juegoOpt = juegoService.obtenerSopaLetrasAccesible(juegoId, email);

        if (juegoOpt.isPresent()) {
            JuegoSopaLetras juego = juegoOpt.get();
            model.addAttribute("juego", juego);

            if (userDetails != null) {
                boolean tieneLike = juegoLikeService.haDadoLike(email, juegoId);
                model.addAttribute("tieneLike", tieneLike);
                model.addAttribute("User", true);
            } else {
                model.addAttribute("tieneLike", false);
                model.addAttribute("User", false);
            }

            model.addAttribute("esAhorcado",false);
            model.addAttribute("esCrucigrama",false);
            model.addAttribute("esSopaLetras", juego instanceof JuegoSopaLetras);


            return "Juegos/Sopa/JugarSopa";
        } else {
            model.addAttribute("error", "El juego no existe.");
            return "redirect:/sopaletras/listar";
        }
    }

    /*
     * Permite al usuario ver los detalles de un juego de sopa de letras.
     * Muestra los cuadernos asociados al juego con paginación.
     */
    @GetMapping("/ver")
    public String verJuego(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(name = "pageCuadernos", defaultValue = "0") int pageCuadernos,
            RedirectAttributes redirectAttributes) {

        Long juegoId = (Long) session.getAttribute("juegoId");

        if (juegoId == null) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/sopaletras/listar";
        }

        Optional<JuegoSopaLetras> juego = juegoSopaLetrasService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isPresent()) {
            model.addAttribute("juego", juego.get());

            int size = 3;
            Page<Cuaderno> cuadernosPage = juegoSopaLetrasService.obtenerCuadernosConJuegoPaginados(juego.get(),
                    pageCuadernos, size);

            boolean pages = cuadernosPage.getTotalPages() > 0;

            model.addAttribute("pages", pages);
            model.addAttribute("cuadernos", cuadernosPage.getContent());
            model.addAttribute("currentPageCuadernos", pageCuadernos + 1);
            model.addAttribute("totalPagesCuadernos", cuadernosPage.getTotalPages());
            model.addAttribute("hasPrevCuadernos", pageCuadernos > 0);
            model.addAttribute("hasNextCuadernos", pageCuadernos < cuadernosPage.getTotalPages() - 1);
            model.addAttribute("prevPageCuadernos", pageCuadernos > 0 ? pageCuadernos - 1 : 0);
            model.addAttribute("nextPageCuadernos",
                    pageCuadernos < cuadernosPage.getTotalPages() - 1 ? pageCuadernos + 1 : pageCuadernos);
            model.addAttribute("fechaFormateada", juego.get().getFechaCreacionFormateada());

            return "Juegos/Sopa/verJuegoSopaLetras";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver este juego.");
            return "redirect:/sopaletras/listar";
        }
    }

    /*
     * Permite al usuario editar un juego de sopa de letras.
     * El juego debe existir y el usuario debe tener permisos para editarlo.
     */
    @PostMapping("/editar")
    public String editarJuego(@RequestParam("juegoId") Long juegoId,
            @ModelAttribute JuegoSopaLetras juegoEditado,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            juegoSopaLetrasService.editarJuego(juegoId, juegoEditado, userDetails.getUsername());

            session.setAttribute("juegoId", juegoId);
            redirectAttributes.addFlashAttribute("mensaje", "Juego editado exitosamente.");

            return "redirect:/sopaletras/ver";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sopaletras/listar";
        }
    }

    /*
     * Redirige a la página de creación del juego.
     */
    @GetMapping("/crear")
    public String crearJuego(Model model) {
        model.addAttribute("juego", new JuegoSopaLetras());
        return "Juegos/Sopa/crearJuegosSopaLetras";
    }

    /*
     * Guarda un nuevo juego de sopa de letras.
     * El juego se guarda con la imagen proporcionada y se asocia al usuario autenticado.
     */
    @PostMapping("/guardar")
    public String guardarJuego(@ModelAttribute JuegoSopaLetras juego,
            @RequestParam("imagenJuego") MultipartFile imagenJuego,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        juegoSopaLetrasService.guardarJuego(juego, userDetails.getUsername(), imagenJuego);
        redirectAttributes.addFlashAttribute("mensaje", "Juego guardado correctamente.");
        return "redirect:/sopaletras/listar";
    }

    /*
     * Elimina un juego de sopa de letras.
     * El usuario debe tener permisos para eliminar el juego.
     */
    @PostMapping("/eliminar")
    public String eliminarJuego(@RequestParam("juegoId") Long juegoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<JuegoSopaLetras> juego = juegoSopaLetrasService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/sopaletras/listar";
        }

        try {
            juegoService.eliminarJuego(juegoId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/sopaletras/listar";
    }

}
