package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.FichaUsuario;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.FichaLikeService;
import com.tfg.SmartPlay.service.FichaService;
import com.tfg.SmartPlay.service.FichaUsuarioService;
import com.tfg.SmartPlay.service.UserComponent;
import com.tfg.SmartPlay.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

// Controlador de las fichas

@Controller
@RequestMapping("/f")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @Autowired
    private CuadernoService cuadernoService;

    @Autowired
    private FichaLikeService fichaLikeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FichaUsuarioService fichaUsuarioService;

    @Autowired
    private UserComponent userComponent;

    // Método para listar las fichas de un usuario

    @GetMapping("/listarFichas")
    public String listarFichas(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<Ficha> fichasPage = fichaService.obtenerFichasPaginadasPorUsuario(userDetails.getUsername(), page, size);

        int totalPages = fichasPage.getTotalPages();
        boolean pages = totalPages > 0;
        boolean hasPrev = page > 0;
        boolean hasNext = page < totalPages - 1;
        int prevPage = hasPrev ? page - 1 : 0;
        int nextPage = hasNext ? page + 1 : page;

        List<Map<String, Object>> fichasProcesadas = fichasPage.getContent().stream().map(ficha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ficha.getId());
            map.put("nombre", ficha.getNombre());
            map.put("asignatura", ficha.getAsignatura());
            map.put("meGusta", ficha.getMeGusta());
            map.put("fechaFormateada", ficha.getFechaCreacionFormateada());
            return map;
        }).toList();

        model.addAttribute("fichas", fichasProcesadas);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("pages", pages);

        return "/Fichas/verFichas";
    }

    // Método para seleccionar una ficha sin revelar id en la URL

    @PostMapping("/seleccionarFicha")
    public String seleccionarFicha(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            session.setAttribute("fichaId", fichaId);
            return "redirect:/f/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada o sin permiso.");
            return "redirect:/f/listarFichas";
        }
    }

    // Método para ver una ficha que lleva a la template

    @GetMapping("/verFicha")
    public String verFichaDesdeSesion(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(name = "pageCuadernos", defaultValue = "0") int pageCuadernos,
            RedirectAttributes redirectAttributes) {

        Long fichaId = (Long) session.getAttribute("fichaId");

        if (fichaId == null) {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            model.addAttribute("ficha", ficha.get());

            int size = 3;
            Page<Cuaderno> cuadernosPage = cuadernoService.obtenerCuadernosConFichaPaginados(ficha.get(), pageCuadernos,
                    size);

            int totalPagesCuadernos = cuadernosPage.getTotalPages();
            boolean pages = totalPagesCuadernos > 0;
            boolean hasPrevCuadernos = pageCuadernos > 0;
            boolean hasNextCuadernos = pageCuadernos < totalPagesCuadernos - 1;
            int prevPageCuadernos = hasPrevCuadernos ? pageCuadernos - 1 : 0;
            int nextPageCuadernos = hasNextCuadernos ? pageCuadernos + 1 : pageCuadernos;

            model.addAttribute("cuadernos", cuadernosPage.getContent());
            model.addAttribute("currentPage", pageCuadernos + 1);
            model.addAttribute("totalPages", totalPagesCuadernos);
            model.addAttribute("hasPrevCuadernos", hasPrevCuadernos);
            model.addAttribute("hasNextCuadernos", hasNextCuadernos);
            model.addAttribute("prevPageCuadernos", prevPageCuadernos);
            model.addAttribute("nextPageCuadernos", nextPageCuadernos);
            model.addAttribute("pages", pages);
            model.addAttribute("fechaFormateada", ficha.get().getFechaCreacionFormateada());

            return "Fichas/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver esta ficha.");
            return "redirect:/f/listarFichas";
        }
    }

    // Método que lleva a la template de crear fichas

    @GetMapping("/crearFichas")
    public String crearfichas(Model model) {
        return "Fichas/crearFichas";
    }

    // Método para obtener la imagen de una ficha

    @GetMapping("/ficha/image/{id}")
    public ResponseEntity<Object> downloadFichaImage(@PathVariable Long id) {
        return fichaService.obtenerImagenFicha(id);
    }

    // Método para guardar una ficha

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute Ficha ficha, @RequestParam("imagenFondo") MultipartFile imagenFondo,
            @AuthenticationPrincipal UserDetails userDetails, HttpSession session,
            RedirectAttributes redirectAttributes) throws Exception {

        fichaService.guardarFicha(ficha, imagenFondo, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/f/listarFichas";
    }

    // Método para editar una ficha

    @PostMapping("/editar")
    public String editarFicha(@RequestParam("fichaId") Long fichaId,
            @ModelAttribute Ficha fichaEditada,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            fichaService.editarFicha(fichaId, fichaEditada, userDetails.getUsername());

            session.setAttribute("fichaId", fichaId);
            redirectAttributes.addFlashAttribute("mensaje", "Ficha editada exitosamente.");

            return "redirect:/f/verFicha";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/f/listarFichas";
        }
    }

    // Método para eliminar una ficha

    @PostMapping("/eliminar")
    public String eliminarFicha(@RequestParam("fichaId") Long fichaId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }

        try {
            fichaService.eliminarFicha(fichaId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/f/listarFichas";
    }

    // Método para modificar una ficha

    @PostMapping("/irModificarFicha")
    public String irAModificarFicha(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            session.setAttribute("fichaId", fichaId);
            return "redirect:/f/modificarFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada o sin permisos.");
            return "redirect:/f/listarFichas";
        }
    }

    // Método para modificar una ficha desde la sesión

    @GetMapping("/modificarFicha")
    public String modificarFichaDesdeSesion(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        Long fichaId = (Long) session.getAttribute("fichaId");

        if (fichaId == null) {
            redirectAttributes.addFlashAttribute("error", "Ficha no seleccionada.");
            return "redirect:/f/listarFichas";
        }

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para modificar esta ficha.");
            return "redirect:/f/listarFichas";
        }

        model.addAttribute("ficha", ficha.get());
        return "Fichas/ModificarFicha";
    }

    // Método para guardar elementos superpuestos en una ficha

    @PostMapping("/guardarElementos")
    public String guardarElementosSuperpuestos(@RequestParam Long fichaId,
            @RequestParam String elementosSuperpuestos,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes,
            HttpSession session, Model model) {
        try {
            session.setAttribute("fichaId", fichaId);
            fichaService.guardarElementosSuperpuestos(fichaId, elementosSuperpuestos, userDetails.getUsername());
            Optional<User> user = userService.findUserByEmail(userDetails.getUsername());

            fichaUsuarioService.guardarNota(fichaId, user, 0.0);

            redirectAttributes.addFlashAttribute("mensaje", "Elementos guardados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar elementos: " + e.getMessage());
        }
        return "redirect:/f/verFichaInteractiva";
    }

    // Método para conseguir una ficha desde la sesión y redirigir a la vista interactiva

    @PostMapping("/conseguirFicha")
    public String conseguirFicha(Model model, @RequestParam Long fichaId, HttpSession session) {
        session.setAttribute("fichaId", fichaId);
        return "redirect:/f/verFichaInteractiva";
    }

    // Método para ver una ficha interactiva, mostrando los elementos superpuestos

    @GetMapping("/verFichaInteractiva")
    public String verFichaInteractiva(Model model, HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {

        Long fichaId = (Long) session.getAttribute("fichaId");

        Optional<Ficha> fichaOptional = fichaService.obtenerFichaPorId(fichaId);

        if (fichaOptional.isPresent()) {
            Ficha ficha = fichaOptional.get();

            ObjectMapper mapper = new ObjectMapper();
            String elementosJson = mapper.writeValueAsString(ficha.getElementosSuperpuestos());

            model.addAttribute("ficha", ficha);
            model.addAttribute("elementosJson", elementosJson);

            if (userDetails != null) {
                boolean tieneLike = fichaLikeService.haDadoLike(userDetails.getUsername(), fichaId);
                model.addAttribute("tieneLike", tieneLike);
                model.addAttribute("esPropietario",
                        ficha.getUsuario().getId().equals(userComponent.getUser().get().getId()));
                model.addAttribute("User", true);
                
                Optional<FichaUsuario> fichaUsuarioOpt = fichaUsuarioService.obtenerNota(
                    ficha.getId(),
                    userComponent.getUser().get().getId()
                );
                model.addAttribute("fichaUsuario", fichaUsuarioOpt.orElse(new FichaUsuario()));
                model.addAttribute("notaGuardada", fichaUsuarioOpt.map(FichaUsuario::getNota).orElse(null));

            
            

            } else {
                model.addAttribute("tieneLike", false);
                model.addAttribute("esPropietario", false);
                model.addAttribute("User", false);

            }

            return "Fichas/verFichaInteractiva";
        } else {
            return "redirect:/f/listarFichas";
        }
    }

    // Método para investigar fichas públicas

    @GetMapping("/investigar")
    public String verFichasPublicas(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false, defaultValue = "fecha") String orden) {

        int size = 24;

        Page<Ficha> fichasPage;
        if ("popularidad".equalsIgnoreCase(orden)) {
            fichasPage = fichaService.ordenarPorMeGusta(buscar, page, size);
        } else {
            fichasPage = fichaService.ordenarPorFecha(buscar, page, size);
        }

        List<Map<String, Object>> fichasProcesadas = fichasPage.getContent().stream().map(ficha -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ficha.getId());
            map.put("nombre", ficha.getNombre());
            map.put("asignatura", ficha.getAsignatura());
            map.put("meGusta", ficha.getMeGusta());
            map.put("fechaFormateada", ficha.getFechaCreacionFormateada());
            return map;
        }).toList();

        model.addAttribute("fichas", fichasProcesadas);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", fichasPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < fichasPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < fichasPage.getTotalPages() - 1 ? page + 1 : page);
        model.addAttribute("pages", fichasPage.getTotalPages() > 0);
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("ordenFecha", "fecha".equalsIgnoreCase(orden));
        model.addAttribute("ordenPopularidad", "popularidad".equalsIgnoreCase(orden));

        return "investigar";
    }

    // Método para dar like a una ficha

    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<String> darMeGusta(
            @RequestParam Long fichaId,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            fichaLikeService.alternarLike(userDetails.getUsername(), fichaId);
            return ResponseEntity.ok("Like registrado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para guardar una nota en una ficha
    
    @PostMapping("/guardarNota")
    @ResponseBody
    public ResponseEntity<?> guardarNota(
        @RequestBody Map<String, Object> datos,
        @AuthenticationPrincipal UserDetails userDetails
    ) throws JsonProcessingException {

        Object fichaIdRaw = datos.get("fichaId");
        Long fichaId = fichaIdRaw instanceof Number
        ? ((Number) fichaIdRaw).longValue()
        : Long.parseLong(fichaIdRaw.toString());
        Object notaRaw = datos.get("nota");
        Double nota = notaRaw instanceof Number
                ? ((Number) notaRaw).doubleValue()
                : Double.parseDouble(notaRaw.toString());


        Optional<User> user = userService.findUserByEmail(userDetails.getUsername());
        fichaUsuarioService.guardarNota(fichaId, user, nota);
    
        return ResponseEntity.ok().build();
    }
    

}
