package com.ays.kardex.service.impl;

import com.ays.kardex.entity.AuditLog;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.repository.AuditLogRepository;
import com.ays.kardex.service.AuditLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void registrarEvento(String action, Map<String, Object> details, Usuario usuario, String ipAddress) {
        if (usuario == null || action == null || ipAddress == null) {
            return;
        }

        AuditLog log = new AuditLog();
        log.setUserId(usuario.getId());
        log.setIpAddress(ipAddress);
        log.setAction(action);
        log.setDetails(convertirADetalleJson(details));
        auditLogRepository.save(log);
    }

    private String convertirADetalleJson(Map<String, Object> details) {
        Map<String, Object> safeDetails = details != null ? details : Collections.emptyMap();
        try {
            return objectMapper.writeValueAsString(safeDetails);
        } catch (JsonProcessingException e) {
            return safeDetails.toString();
        }
    }
}
