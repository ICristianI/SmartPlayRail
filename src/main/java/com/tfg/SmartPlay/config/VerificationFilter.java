package com.tfg.SmartPlay.config;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.UserRepository;

import org.springframework.stereotype.Component;

// Filtro que verifica si el usuario está verificado
// Si no lo está, se redirige a la página de verificación

@Component
public class VerificationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    @SuppressWarnings("unused")
    private final UserDetailsService userDetailsService;

    public VerificationFilter(UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String rq = request.getRequestURI();

        if (rq.equals("/signup") || rq.equals("/users/register") || rq.equals("/") || rq.equals("/users/delete")
                || rq.equals("/users/update") || rq.equals("/users/verificar") || rq.equals("/users/verificar/**")
                || rq.equals("/users/resend") || rq.equals("/send") || rq.equals("/error") || rq.equals("/css/**")
                || rq.equals("/js/**") || rq.equals("/images/**") || rq.equals("/json/**") || rq.equals("/config")
                || rq.equals("/msj") || rq.equals("/regin") || rq.equals("/verify") || rq.equals("/users/verificar")
                || rq.equals("/users/verificar/**") || rq.equals("/users/resend")) {
            chain.doFilter(request, response);
            return;
        }

        String email = request.getParameter("email");

        if (email != null) {
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent() && !user.get().isEnabled()) {
                response.sendRedirect("/verify?error=Usuario no verificado");
                return;
            } else if (!user.isPresent()) {
                response.sendRedirect("/login?error=Usuario no encontrado");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
