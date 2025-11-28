package com.ays.kardex.service;

import com.ays.kardex.dto.sede.SedeRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.dto.sede.SedeUpdateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;

import java.util.List;

public interface SedeService {

    SedeResponse crear(SedeRequest request);

    SedeResponse obtener(Long id);

    SedeResponse actualizar(Long id, SedeUpdateRequest request);

    void inactivar(Long id);

    List<UsuarioResponse> listarUsuariosActivos(Long sedeId);
}
