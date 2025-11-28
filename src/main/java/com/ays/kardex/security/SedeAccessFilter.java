package com.ays.kardex.security;

import com.ays.kardex.entity.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SedeAccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
                if (usuario.getRole() == Usuario.Role.VENDEDOR) {
                    Long sedeId = usuario.getSede() != null ? usuario.getSede().getId() : null;
                    SedeContext.setSedeId(sedeId);
                    if (!isSedeAllowed(request, sedeId)) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"mensaje\":\"Acceso denegado a otra sede\"}");
                        return;
                    }
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            SedeContext.clear();
        }
    }

    private boolean isSedeAllowed(HttpServletRequest request, Long sedeId) {
        String path = request.getRequestURI();
        if (path.startsWith("/api/kardex/sede/")) {
            return validarUltimoSegmento(path, sedeId);
        }
        if (path.startsWith("/api/sedes/")) {
            return validarSegmentoSede(path, sedeId);
        }
        return true;
    }

    private boolean validarSegmentoSede(String path, Long sedeId) {
        String[] segments = path.split("/");
        if (segments.length > 3) {
            try {
                Long requestedSede = Long.parseLong(segments[3]);
                return requestedSede.equals(sedeId);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return true;
    }

    private boolean validarUltimoSegmento(String path, Long sedeId) {
        try {
            String[] segments = path.split("/");
            String idSegment = segments[segments.length - 1];
            Long requestedSede = Long.parseLong(idSegment);
            return requestedSede.equals(sedeId);
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
