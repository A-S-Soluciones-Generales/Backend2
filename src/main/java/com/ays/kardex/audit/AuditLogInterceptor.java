package com.ays.kardex.audit;

import com.ays.kardex.audit.AuditLogContext.AuditData;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuditLogInterceptor implements HandlerInterceptor {

    private final AuditLogService auditLogService;

    public AuditLogInterceptor(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            AuditData auditData = AuditLogContext.getCurrent();
            if (auditData != null) {
                Usuario usuario = obtenerUsuarioAutenticado();
                if (usuario != null) {
                    String ipAddress = request.getRemoteAddr();
                    auditLogService.registrarEvento(auditData.action(), auditData.details(), usuario, ipAddress);
                }
            }
        } finally {
            AuditLogContext.clear();
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario usuario) {
            return usuario;
        }
        return null;
    }
}
