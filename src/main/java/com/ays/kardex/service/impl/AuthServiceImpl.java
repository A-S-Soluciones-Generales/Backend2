package com.ays.kardex.service.impl;

import com.ays.kardex.dto.auth.AuthResponse;
import com.ays.kardex.dto.auth.LoginRequest;
import com.ays.kardex.dto.auth.RegistroRequest;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.exception.BadRequestException;
import com.ays.kardex.repository.UsuarioRepository;
import com.ays.kardex.security.JwtService;
import com.ays.kardex.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Usuario.Role.USER)
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Generar token JWT
        String jwtToken = jwtService.generateToken(usuarioGuardado);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(usuarioGuardado.getId())
                .email(usuarioGuardado.getEmail())
                .nombre(usuarioGuardado.getNombre())
                .apellido(usuarioGuardado.getApellido())
                .role(usuarioGuardado.getRole().name())
                .mensaje("Usuario registrado exitosamente")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Email o contraseña incorrectos");
        }

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new BadRequestException("La cuenta está desactivada");
        }

        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .role(usuario.getRole().name())
                .mensaje("Inicio de sesión exitoso")
                .build();
    }
}
