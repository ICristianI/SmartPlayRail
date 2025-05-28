package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.service.JuegoCrucigramaService;
import com.tfg.SmartPlay.service.JuegoLikeService;
import com.tfg.SmartPlay.service.JuegoService;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Controller para manejar las acciones relacionadas con el juego de crucigrama.

@Controller
@RequestMapping("/crucigrama")
public class JuegoCrucigramaController {

    @Autowired
    private JuegoLikeService juegoLikeService;
    
    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoCrucigramaService juegoCrucigramaService;

    /*
    * Listar juegos de crucigrama del usuario autenticado con paginación.
    */ 
    @GetMapping("/listar")
    public String listarJuegos(Model model,
                               @AuthenticationPrincipal UserDetails userDetails,
                               @RequestParam(defaultValue = "0") int page) {
        int size = 6;
        Page<JuegoCrucigrama> juegosPage = juegoCrucigramaService.obtenerJuegosPaginadosPorUsuario(userDetails.getUsername(), page, size);

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


        return "Juegos/Crucigrama/verJuegosCrucigrama";
    }

    /*
     * Permite al usuario acceder a un juego de crucigrama específico.
     * Verifica si el juego es accesible para el usuario autenticado.
     */
    @GetMapping("/jugar")
    public String jugarCrucigrama(Model model, HttpSession session,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        Long juegoId = (Long) session.getAttribute("juegoId");
    
        String email = (userDetails != null) ? userDetails.getUsername() : null;
        Optional<JuegoCrucigrama> juegoOpt = juegoService.obtenerCrucigramaAccesible(juegoId, email);
    
        if (juegoOpt.isPresent()) {
            JuegoCrucigrama juego = juegoOpt.get();
            model.addAttribute("juego", juego);
    
            if (userDetails != null) {
                boolean tieneLike = juegoLikeService.haDadoLike(userDetails.getUsername(), juegoId);
                model.addAttribute("tieneLike", tieneLike);
                model.addAttribute("User", true);
            } else {
                model.addAttribute("tieneLike", false);
                model.addAttribute("User", false);
            }

            model.addAttribute("esAhorcado", false);
            model.addAttribute("esCrucigrama", juego instanceof JuegoCrucigrama);
            model.addAttribute("esSopaLetras",  false);

    
            return "Juegos/Crucigrama/JugarCrucigrama";
        } else {
            model.addAttribute("error", "El juego no existe.");
            return "redirect:/crucigrama/listar";
        }
    }
    

    /*
     * Permite al usuario ver los detalles de un juego de crucigrama específico.
     * Verifica si el usuario tiene permisos para ver el juego.
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
            return "redirect:/crucigrama/listar";
        }
    
        Optional<JuegoCrucigrama> juego = juegoCrucigramaService.obtenerJuego(juegoId, userDetails.getUsername());
    
        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver este juego.");
            return "redirect:/crucigrama/listar";
        }
    
        model.addAttribute("juego", juego.get());
    
        // Manejo de la paginación de cuadernos
        int size = 3;
        Page<Cuaderno> cuadernosPage = juegoCrucigramaService.obtenerCuadernosConJuegoPaginados(juego.get(), pageCuadernos, size);
    
        boolean pages = cuadernosPage.getTotalPages() > 0;

        model.addAttribute("pages", pages);
        model.addAttribute("cuadernos", cuadernosPage.getContent());
        model.addAttribute("currentPageCuadernos", pageCuadernos + 1);
        model.addAttribute("totalPagesCuadernos", cuadernosPage.getTotalPages());
        model.addAttribute("hasPrevCuadernos", pageCuadernos > 0);
        model.addAttribute("hasNextCuadernos", pageCuadernos < cuadernosPage.getTotalPages() - 1);
        model.addAttribute("prevPageCuadernos", Math.max(0, pageCuadernos - 1));
        model.addAttribute("nextPageCuadernos", Math.min(pageCuadernos + 1, cuadernosPage.getTotalPages() - 1));
        model.addAttribute("fechaFormateada", juego.get().getFechaCreacionFormateada());

    
        // Procesar pistas y respuestas
        String pistasRaw = juego.get().getPistas();
        String respuestasRaw = juego.get().getRespuestas();
        
        List<String> pistas = (pistasRaw != null && !pistasRaw.isEmpty()) ? List.of(pistasRaw.split(",")) : new ArrayList<>();
        List<String> respuestas = (respuestasRaw != null && !respuestasRaw.isEmpty()) ? List.of(respuestasRaw.split(",")) : new ArrayList<>();
        
        List<Map<String, String>> pistasRespuestas = new ArrayList<>();
        for (int i = 0; i < pistas.size(); i++) {
            Map<String, String> par = new HashMap<>();
            par.put("pista", pistas.get(i));
            par.put("respuesta", (i < respuestas.size()) ? respuestas.get(i) : "");
            pistasRespuestas.add(par);
        }
        
        model.addAttribute("pistasRespuestas", pistasRespuestas);
        System.out.println(pistasRespuestas);        
    
        return "Juegos/Crucigrama/verJuegoCrucigrama";
    }
    


    /*
     *  Permite editar el juego de crucigrama.
     */
    @PostMapping("/editar")
    public String editarJuego(@RequestParam("juegoId") Long juegoId,
                              @ModelAttribute JuegoCrucigrama juegoEditado,
                              @AuthenticationPrincipal UserDetails userDetails,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            juegoCrucigramaService.editarJuego(juegoId, juegoEditado, userDetails.getUsername());
            session.setAttribute("juegoId", juegoId);
            redirectAttributes.addFlashAttribute("mensaje", "Juego editado exitosamente.");
            return "redirect:/crucigrama/ver";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/crucigrama/listar";
        }
    }

    /*
     * Redirige a la página para crear un nuevo juego de crucigrama.
     */
    @GetMapping("/crear")
    public String crearJuego(Model model) {
        model.addAttribute("juego", new JuegoCrucigrama());
        return "Juegos/Crucigrama/crearJuegoCrucigrama";
    }
    
    /*
     * Guarda un nuevo juego de crucigrama.
     * Procesa la imagen del juego y guarda los datos en la base de datos.
     */
    @PostMapping("/guardar")
    public String guardarJuego(@ModelAttribute JuegoCrucigrama juego,
                           @RequestParam("imagenJuego") MultipartFile imagenJuego,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        juegoCrucigramaService.guardarJuego(juego, userDetails.getUsername(), imagenJuego);
        redirectAttributes.addFlashAttribute("mensaje", "Juego guardado correctamente.");
        return "redirect:/crucigrama/listar";
    }


    /*
     * Elimina un juego de crucigrama.
     * Verifica si el usuario tiene permisos para eliminar el juego.
     */
    @PostMapping("/eliminar")
    public String eliminarJuego(@RequestParam("juegoId") Long juegoId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Optional<JuegoCrucigrama> juego = juegoCrucigramaService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/crucigrama/listar";
        }

        try {
            juegoService.eliminarJuego(juegoId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/crucigrama/listar";
    }
}
