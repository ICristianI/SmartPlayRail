package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Grupo;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.CuadernoUsuarioService;
import com.tfg.SmartPlay.service.GrupoService;
import com.tfg.SmartPlay.service.UserComponent;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador para manejar las operaciones relacionadas con los grupos de usuarios

@Controller
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private CuadernoService cuadernoService;

    @Autowired
    private CuadernoUsuarioService cuadernoUsuarioService;

    // Lista todos los grupos del usuario

    @GetMapping
    public String listarGrupos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {
        int size = 6;
        Page<Grupo> gruposPage = grupoService.listarGruposPaginados(userDetails.getUsername(), page, size);

        model.addAttribute("totalPages", gruposPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < gruposPage.getTotalPages() - 1);
        model.addAttribute("prevPage", Math.max(page - 1, 0));
        model.addAttribute("nextPage", Math.min(page + 1, gruposPage.getTotalPages() - 1));
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("pagesG", gruposPage.getTotalPages() > 0);

        List<Map<String, Object>> gruposProcesados = gruposPage.getContent().stream().map(grupo -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", grupo.getId());
            map.put("nombre", grupo.getNombre());
            map.put("fechaFormateada", grupo.getFechaCreacionFormateada());
            map.put("numUsuarios", grupo.getUsuarios() != null ? grupo.getUsuarios().size() : 0);
            map.put("numCuadernos", grupo.getCuadernos() != null ? grupo.getCuadernos().size() : 0);
            return map;
        }).toList();

        model.addAttribute("grupos", gruposProcesados);

        return "Grupos/verGrupos";
    }

    // Formulario para crear grupo

    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("grupo", new Grupo());
        model.addAttribute("cuaderno",
                cuadernoService.listarCuadernosPorUsuario(userComponent.getUser().get().getEmail()));
        return "Grupos/crearGrupos";
    }

    // Guardar grupo

    @PostMapping("/guardar")
    public String guardarGrupo(@ModelAttribute Grupo grupo,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("cuadernosSeleccionados") List<Long> cuadernosId) {

        grupoService.guardarGrupo(grupo, userDetails.getUsername(), cuadernosId);
        redirectAttributes.addFlashAttribute("mensaje", "Grupo creado correctamente.");
        return "redirect:/grupos";
    }

    @GetMapping("/unirse")
    public String mostrarFormularioUnirse(Model model) {
        return "Grupos/unirseGrupo";
    }

    // Unirse a grupo por código

    @PostMapping("/unirse")
    public String unirseAGrupo(@RequestParam("codigoAcceso") String codigo,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean unido = grupoService.unirseAGrupo(codigo, userDetails.getUsername());

        if (unido) {
            redirectAttributes.addFlashAttribute("mensaje", "Te has unido al grupo.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Código inválido o ya perteneces al grupo.");
        }

        return "redirect:/grupos";
    }

    // Ver detalles del grupo

    @PostMapping("/ver")
    public String verGrupoPost(@RequestParam("grupoId") Long grupoId, HttpSession session) {
        session.setAttribute("grupoId", grupoId);
        return "redirect:/grupos/ver";
    }

    @GetMapping("/ver")
    public String verGrupoGet(Model model,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int pageCuadernos,
            @RequestParam(defaultValue = "0") int pageUsuarios,
            @RequestParam(defaultValue = "3") int size,
            RedirectAttributes redirectAttributes) {

        Long grupoId = (Long) session.getAttribute("grupoId");

        if (grupoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el grupo.");
            return "redirect:/grupos";
        }

        Grupo grupo = grupoService.obtenerGrupo(grupoId, userDetails.getUsername());

        if (grupo == null) {
            redirectAttributes.addFlashAttribute("error", "No tienes acceso a este grupo.");
            return "redirect:/grupos";
        }

        boolean esCreador = grupo.getCreador().getEmail().equals(userDetails.getUsername());
        model.addAttribute("esCreador", esCreador);
        model.addAttribute("fechaFormateada", grupo.getFechaCreacionFormateada());

        Page<Cuaderno> cuadernosPage = grupoService.obtenerCuadernosPaginados(grupoId, pageCuadernos, size);
        User user = userComponent.getUser().orElse(null);

        for (Cuaderno cuaderno : cuadernosPage.getContent()) {
            cuadernoUsuarioService.actualizarNota(cuaderno.getId(), Optional.ofNullable(user));
        }
        
        Map<Long, Double> notasPorCuaderno = new HashMap<>();

        for (Cuaderno cuaderno : cuadernosPage.getContent()) {
            cuadernoUsuarioService.obtenerNota(cuaderno.getId(), user.getId())
                .ifPresent(cuadernoUsuario -> notasPorCuaderno.put(cuaderno.getId(), cuadernoUsuario.getNota()));
        }

        

        model.addAttribute("notasPorCuaderno", notasPorCuaderno);

        List<Map<String, Object>> cuadernosConNota = cuadernosPage.getContent().stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nombre", c.getNombre());
            map.put("numeroFichas", c.getFichas().size());
            map.put("numeroJuegos", c.getJuegos().size());
            cuadernoUsuarioService.obtenerNota(c.getId(), user.getId())
                .ifPresent(cuadernoUsuario -> map.put("nota", cuadernoUsuario.getNota()));
            return map;
        }).toList();
        
        model.addAttribute("cuadernosPage", cuadernosConNota);
        model.addAttribute("currentPageCuadernos", pageCuadernos + 1);
        model.addAttribute("totalPagesCuadernos", cuadernosPage.getTotalPages());
        model.addAttribute("hasPrevCuadernos", pageCuadernos > 0);
        model.addAttribute("hasNextCuadernos", pageCuadernos < cuadernosPage.getTotalPages() - 1);
        model.addAttribute("prevPageCuadernos", Math.max(pageCuadernos - 1, 0));
        model.addAttribute("nextPageCuadernos", Math.min(pageCuadernos + 1, cuadernosPage.getTotalPages() - 1));
        model.addAttribute("pagesC", cuadernosPage.getTotalPages() > 0);

        Page<User> usuariosPage = grupoService.obtenerUsuariosPaginados(grupoId, pageUsuarios, size);

        List<Map<String, Object>> usuariosProcesados = usuariosPage.getContent().stream().map(u -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("nombre", u.getNombre());
            map.put("roles", u.getRoles());
            map.put("esPropietarioActual", u.getId().equals(userComponent.getUser().get().getId()));
            return map;
        }).toList();

        model.addAttribute("usuariosPage", usuariosProcesados);
        model.addAttribute("currentPageUsuarios", pageUsuarios + 1);
        model.addAttribute("totalPagesUsuarios", usuariosPage.getTotalPages());
        model.addAttribute("hasPrevUsuarios", pageUsuarios > 0);
        model.addAttribute("hasNextUsuarios", pageUsuarios < usuariosPage.getTotalPages() - 1);
        model.addAttribute("prevPageUsuarios", Math.max(pageUsuarios - 1, 0));
        model.addAttribute("nextPageUsuarios", Math.min(pageUsuarios + 1, usuariosPage.getTotalPages() - 1));
        model.addAttribute("pagesU", usuariosPage.getTotalPages() > 0);

        model.addAttribute("grupo", grupo);
        model.addAttribute("cuadernosDisponibles",
                grupoService.obtenerCuadernosNoAgregados(grupo, userDetails.getUsername()));
        model.addAttribute("numUsuarios", grupo.getUsuarios().size());
        model.addAttribute("numCuadernos", grupo.getCuadernos().size());

        model.addAttribute("isPropietario", grupoService.isPropietario(grupo, userDetails.getUsername()));

        return "Grupos/verGrupo";
    }

    // Editar grupo

    @PostMapping("/editar")
    public String editarGrupo(@RequestParam("grupoId") Long grupoId,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "cuadernosSeleccionados", required = false) List<Long> cuadernosIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean editado = grupoService.editarGrupo(grupoId, nombre, descripcion, cuadernosIds,
                userDetails.getUsername());

        if (editado) {
            redirectAttributes.addFlashAttribute("mensaje", "Grupo editado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este grupo.");
        }

        return "redirect:/grupos/ver";
    }

    // Eliminar grupo

    @PostMapping("/eliminar")
    public String eliminarGrupo(@RequestParam("grupoId") Long grupoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = grupoService.eliminarGrupo(grupoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Grupo eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este grupo.");
        }

        return "redirect:/grupos";
    }

    // Eliminar cuaderno del grupo

    @PostMapping("/eliminarCuaderno")
    public String eliminarCuadernoDelGrupo(@RequestParam("grupoId") Long grupoId,
            @RequestParam("cuadernoId") Long cuadernoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = grupoService.eliminarCuadernoDelGrupo(grupoId, cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Cuaderno eliminado del grupo correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No tienes permiso para modificar este grupo o el cuaderno no está en él.");
        }

        return "redirect:/grupos/ver";
    }

    // Eliminar usuario del grupo

    @PostMapping("/eliminarUsuario")
    public String eliminarUsuarioDelGrupo(@RequestParam("grupoId") Long grupoId,
            @RequestParam("usuarioId") Long usuarioId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = grupoService.eliminarUsuarioDelGrupo(grupoId, usuarioId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado del grupo correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "No tienes permiso para modificar este grupo o el usuario no pertenece a él.");
        }

        return "redirect:/grupos/ver";
    }


}
