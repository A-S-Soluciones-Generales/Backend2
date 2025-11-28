package com.ays.kardex.service;

import com.ays.kardex.dto.auth.UsuarioCreateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioCreateRequest request);
}
