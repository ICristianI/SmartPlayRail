package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.FichaService;
import com.tfg.SmartPlay.service.UserComponent;
import com.tfg.SmartPlay.service.JuegoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Controlador de cuadernos

@Controller
@RequestMapping("/cuadernos")
public class CuadernoController {

    @Autowired
    private CuadernoService cuadernoService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private FichaService fichaService;

    @Autowired
    private JuegoService juegoService;

    /**
     * Lista los cuadernos del usuario autenticado.
     */
    @GetMapping
    public String listarCuadernos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<Cuaderno> cuadernosPage = cuadernoService.listarCuadernosPaginados(userDetails.getUsername(), page, size);

        int totalPages = cuadernosPage.getTotalPages();
        boolean hasPrev = page > 0;
        boolean hasNext = page < totalPages - 1;
        int prevPage = hasPrev ? page - 1 : 0;
        int nextPage = hasNext ? page + 1 : page;
        boolean pagesC = cuadernosPage.getTotalPages() > 0;

        model.addAttribute("pagesC", pagesC);
        model.addAttribute("cuadernos", cuadernosPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);

        List<Map<String, Object>> cuadernoProcesado = cuadernosPage.getContent().stream().map(ficha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ficha.getId());
            map.put("nombre", ficha.getNombre());
            map.put("numeroFichas", ficha.getNumeroFichas());
            map.put("numeroJuegos", ficha.getNumeroJuegos());
            map.put("fechaFormateada", ficha.getFechaCreacionFormateada());
            return map;
        }).toList();

        model.addAttribute("cuadernos", cuadernoProcesado);

        return "Cuadernos/verCuadernos";
    }

    /**
     * POST: Guarda el cuadernoId en sesión y redirige a GET /ver.
     */
    @PostMapping("/ver")
    public String verCuadernoPost(@RequestParam("cuadernoId") Long cuadernoId, HttpSession session) {
        session.setAttribute("cuadernoId", cuadernoId);
        return "redirect:/cuadernos/ver";
    }

    /**
     * GET: Obtiene el cuaderno desde sesión y lo muestra.
     */
    @GetMapping("/ver")
    public String verCuadernoGet(Model model,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int pageFichas,
            @RequestParam(defaultValue = "0") int pageJuegos,
            @RequestParam(defaultValue = "3") int size,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        Optional<Cuaderno> cuaderno = cuadernoService.obtenerCuadernoPorIdYUsuario(cuadernoId,
                userDetails.getUsername());

        if (cuaderno.isPresent()) {
            Cuaderno cuadernoObj = cuaderno.get();

            // Obtener fichas y juegos no agregados
            List<Ficha> fichasNoAgregadas = cuadernoService.obtenerFichasNoAgregadas(cuadernoId,
                    userDetails.getUsername());
            List<Juego> juegosNoAgregados = cuadernoService.obtenerJuegosNoAgregados(cuadernoId,
                    userDetails.getUsername());

            // Obtener fichas y juegos paginados
            Page<Ficha> fichasPage = fichaService.obtenerFichasPaginadas(cuadernoId, pageFichas, size);
            Page<Juego> juegosPage = cuadernoService.obtenerJuegosPaginados(cuadernoId, pageJuegos, size);

            boolean pagesF = fichasPage.getTotalPages() > 0;
            boolean pagesJ = juegosPage.getTotalPages() > 0;

            // Datos de paginación independientes para fichas y juegos
            model.addAttribute("cuaderno", cuadernoObj);

            // Paginación de fichas
            model.addAttribute("fichasPage", fichasPage.getContent());
            model.addAttribute("currentPageFichas", pageFichas + 1);
            model.addAttribute("totalPagesFichas", fichasPage.getTotalPages());
            model.addAttribute("hasPrevFichas", pageFichas > 0);
            model.addAttribute("hasNextFichas", pageFichas < fichasPage.getTotalPages() - 1);
            model.addAttribute("prevPageFichas", pageFichas > 0 ? pageFichas - 1 : 0);
            model.addAttribute("nextPageFichas",
                    pageFichas < fichasPage.getTotalPages() - 1 ? pageFichas + 1 : pageFichas);
            model.addAttribute("pagesF", pagesF);

            // Paginación de juegos
            model.addAttribute("juegosPage", juegosPage.getContent());
            model.addAttribute("currentPageJuegos", pageJuegos + 1);
            model.addAttribute("totalPagesJuegos", juegosPage.getTotalPages());
            model.addAttribute("hasPrevJuegos", pageJuegos > 0);
            model.addAttribute("hasNextJuegos", pageJuegos < juegosPage.getTotalPages() - 1);
            model.addAttribute("prevPageJuegos", pageJuegos > 0 ? pageJuegos - 1 : 0);
            model.addAttribute("nextPageJuegos",
                    pageJuegos < juegosPage.getTotalPages() - 1 ? pageJuegos + 1 : pageJuegos);
            model.addAttribute("pagesJ", pagesJ);

            // Listas de fichas y juegos no agregados
            model.addAttribute("fichasNoAgregadas", fichasNoAgregadas);
            model.addAttribute("juegosNoAgregados", juegosNoAgregados);

            model.addAttribute("fechaFormateada", cuaderno.get().getFechaCreacionFormateada());

            return "Cuadernos/verCuaderno";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para ver este cuaderno.");
            return "redirect:/cuadernos";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo cuaderno.
     */
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        User usuario = userComponent.getUser().get();
        model.addAttribute("cuaderno", new Cuaderno());
        model.addAttribute("fichas", fichaService.obtenerFichasPorUsuario(usuario.getEmail()));
        model.addAttribute("juegos", juegoService.obtenerTodosLosJuegosPorUsuario(usuario.getEmail()));
        return "Cuadernos/crearCuadernos";
    }

    /**
     * Guarda un nuevo cuaderno con las fichas y juegos seleccionados.
     */
    @PostMapping("/guardar")
    public String guardarCuaderno(@ModelAttribute Cuaderno cuaderno,
            @RequestParam("fichasSeleccionadas") List<Long> fichasIds,
            @RequestParam("juegosSeleccionados") List<Long> juegosIds,
            @RequestParam(value = "imagenCuaderno", required = false) MultipartFile imagenCuaderno,
            RedirectAttributes redirectAttributes) {

        try {
            cuadernoService.guardarCuaderno(cuaderno, fichasIds, juegosIds, imagenCuaderno);
        } catch (IOException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("mensaje", "Cuaderno guardado correctamente.");
        return "redirect:/cuadernos";
    }

    /**
     * Obtiene la imagen del cuaderno.
     */
     
    @GetMapping("/image/{id}")
    public ResponseEntity<Object> downloadCuadernoImage(@PathVariable Long id) {
        return cuadernoService.obtenerImagenCuaderno(id);
    }

    /**
     * Editar un cuaderno.
     */

    @PostMapping("/editar")
    public String editarCuaderno(@RequestParam("cuadernoId") Long cuadernoId,
            @RequestParam("nombre") String nuevoNombre,
            @RequestParam(value = "fichasSeleccionadas", required = false) List<Long> fichasIds,
            @RequestParam(value = "juegosSeleccionados", required = false) List<Long> juegosIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean editado = cuadernoService.editarCuaderno(cuadernoId, nuevoNombre, fichasIds, juegosIds,
                userDetails.getUsername());

        if (editado) {
            redirectAttributes.addFlashAttribute("mensaje", "Cuaderno editado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este cuaderno.");
        }

        return "redirect:/cuadernos/ver";
    }

    /**
     * Elimina una ficha del cuaderno sin exponer el ID en la URL.
     */
    @PostMapping("/eliminarFicha")
    public String eliminarFichaDeCuaderno(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        boolean eliminado = cuadernoService.eliminarFichaDeUsuario(fichaId, cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la ficha.");
        }

        return "redirect:/cuadernos/ver";
    }

    /**
     * Elimina un juego del cuaderno sin exponer el ID en la URL.
     */

    @PostMapping("/eliminarJuego")
    public String eliminarJuegoDeCuaderno(@RequestParam("juegoId") Long juegoId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        boolean eliminado = cuadernoService.eliminarJuegoDeUsuario(juegoId, cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el juego.");
        }

        return "redirect:/cuadernos/ver";
    }

    /**
     * Elimina un cuaderno sin exponer el ID en la URL.
     */

    @PostMapping("/eliminar")
    public String eliminarCuaderno(@RequestParam("cuadernoId") Long cuadernoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = cuadernoService.eliminarCuaderno(cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Cuaderno eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este cuaderno.");
        }

        return "redirect:/cuadernos";
    }

    /**
     * Encuentra un cuaderno para resolverlo sin exponer su id.
     */
    @PostMapping("/resolver")
    public String resolverCuadernoPost(@RequestParam("cuadernoId") Long cuadernoId, HttpSession session) {
        session.setAttribute("cuadernoId", cuadernoId);
        return "redirect:/cuadernos/resolver";
    }

    /**
     * Resuelve un cuaderno mostrando una ficha a la vez.
     * Si el cuaderno no tiene fichas, muestra un mensaje de error.
     */
    @GetMapping("/resolver")
    public String resolverCuadernoGet(Model model,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            model.addAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        Optional<Cuaderno> cuadernoOpt = cuadernoService.obtenerCuadernoPorId(cuadernoId);
        if (cuadernoOpt.isEmpty()) {
            model.addAttribute("error", "Cuaderno no encontrado.");
            return "redirect:/cuadernos";
        }

        Cuaderno cuaderno = cuadernoOpt.get();
        List<Ficha> fichas = cuaderno.getFichas();

        if (fichas.isEmpty()) {
            model.addAttribute("error", "Este cuaderno no tiene fichas.");
            return "redirect:/cuadernos";
        }

        int totalFichas = fichas.size();
        int paginaActual = Math.max(0, Math.min(page, totalFichas - 1));
        Ficha fichaActual = fichas.get(paginaActual);

        model.addAttribute("cuaderno", cuaderno);
        model.addAttribute("ficha", fichaActual);
        model.addAttribute("hasPrev", paginaActual > 0);
        model.addAttribute("hasNext", paginaActual < totalFichas - 1);
        model.addAttribute("prevPage", paginaActual - 1);
        model.addAttribute("nextPage", paginaActual + 1);
        model.addAttribute("paginaActual", paginaActual + 1);
        model.addAttribute("totalFichas", totalFichas);

        return "Cuadernos/resolverCuaderno";
    }

    /**
     * Encuentra un cuaderno para resolver juegos sin exponer su id.
     */
    @PostMapping("/resolverJuegos")
    public String resolverJuegosPost(@RequestParam("cuadernoId") Long cuadernoId, HttpSession session) {
        session.setAttribute("cuadernoId", cuadernoId);
        return "redirect:/cuadernos/resolverJuegos";
    }
    

    /**
     * Resuelve los juegos de un cuaderno mostrando un juego a la vez.
     * Si el cuaderno no tiene juegos, muestra un mensaje de error.
     */
    @GetMapping("/resolverJuegos")
    public String resolverJuegosGet(Model model,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        Optional<Cuaderno> cuadernoOpt = cuadernoService.obtenerCuadernoPorId(cuadernoId);
        if (cuadernoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cuaderno no encontrado.");
            return "redirect:/cuadernos";
        }

        Cuaderno cuaderno = cuadernoOpt.get();
        List<Juego> juegos = cuaderno.getJuegos();

        if (juegos.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Este cuaderno no tiene juegos.");
            return "redirect:/cuadernos/ver";
        }

        int totalJuegos = juegos.size();
        int paginaActual = Math.max(0, Math.min(page, totalJuegos - 1));
        Juego juegoActual = juegos.get(paginaActual);

        String tipo;
        if (juegoActual instanceof JuegoAhorcado) {
            tipo = "ahorcado";
        } else if (juegoActual instanceof JuegoSopaLetras) {
            tipo = "sopaletras";
        } else if (juegoActual instanceof JuegoCrucigrama) {
            tipo = "crucigrama";
        } else {
            redirectAttributes.addFlashAttribute("error", "Tipo de juego no reconocido.");
            return "redirect:/cuadernos/ver";
        }

        model.addAttribute("cuaderno", cuaderno);
        model.addAttribute("juego", juegoActual);
        model.addAttribute("tipoJuego", tipo);
        model.addAttribute("paginaActual", paginaActual + 1);
        model.addAttribute("totalJuegos", totalJuegos);
        model.addAttribute("hasPrev", paginaActual > 0);
        model.addAttribute("hasNext", paginaActual < totalJuegos - 1);
        model.addAttribute("prevPage", paginaActual - 1);
        model.addAttribute("nextPage", paginaActual + 1);
        model.addAttribute("esAhorcado", juegoActual instanceof JuegoAhorcado);
        model.addAttribute("esCrucigrama", juegoActual instanceof JuegoCrucigrama);
        model.addAttribute("esSopaLetras", juegoActual instanceof JuegoSopaLetras);


        return "Juegos/resolverJuego";
    }

}
