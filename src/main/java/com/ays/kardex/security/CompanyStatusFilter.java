package com.ays.kardex.security;

import com.ays.kardex.entity.Company;
import com.ays.kardex.entity.Sede;
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
public class CompanyStatusFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            Company company = usuario.getCompany();
            if (company != null && Boolean.FALSE.equals(company.getActivo())) {
                responderBloqueo(response, "La empresa está inactiva");
                return;
            }

            Sede sede = usuario.getSede();
            if (sede != null && Boolean.FALSE.equals(sede.getActivo())) {
                responderBloqueo(response, "La sede está inactiva");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void responderBloqueo(HttpServletResponse response, String mensaje) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"mensaje\":\"" + mensaje + "\"}");
    }
}
