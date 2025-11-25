package com.ays.kardex.service;

import com.ays.kardex.dto.auth.AuthResponse;
import com.ays.kardex.dto.auth.LoginRequest;
import com.ays.kardex.dto.auth.RegistroRequest;

public interface AuthService {

    AuthResponse registrar(RegistroRequest request);

    AuthResponse login(LoginRequest request);
}
