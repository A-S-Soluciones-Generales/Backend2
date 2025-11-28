package com.ays.kardex.service;

import com.ays.kardex.entity.Usuario;
import java.util.Map;

public interface AuditLogService {

    void registrarEvento(String action, Map<String, Object> details, Usuario usuario, String ipAddress);
}
