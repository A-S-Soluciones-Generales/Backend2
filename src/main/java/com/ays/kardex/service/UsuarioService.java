package com.ays.kardex.service;

import com.ays.kardex.dto.auth.UsuarioCreateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.auth.UsuarioUpdateRequest;
import java.util.List;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioCreateRequest request);

    UsuarioResponse actualizarUsuario(Long id, UsuarioUpdateRequest request);

    void inactivarUsuario(Long id);

    List<UsuarioResponse> buscarUsuarios(String search);

    List<UsuarioResponse> listarUsuariosPorCompany(Long companyId);
}
