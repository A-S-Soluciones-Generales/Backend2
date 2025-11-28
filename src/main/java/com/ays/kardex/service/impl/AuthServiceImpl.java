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

import java.util.HashMap;
import java.util.Map;

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
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        if (usuarioRepository.existsByRole(Usuario.Role.GLOBAL_ADMIN)) {
            throw new BadRequestException("Ya existe un administrador global registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Usuario.Role.GLOBAL_ADMIN)
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Map<String, Object> claims = construirClaims(usuarioGuardado);
        String jwtToken = jwtService.generateToken(claims, usuarioGuardado);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(usuarioGuardado.getId())
                .email(usuarioGuardado.getEmail())
                .nombre(usuarioGuardado.getNombre())
                .apellido(usuarioGuardado.getApellido())
                .role(usuarioGuardado.getRole().name())
                .companyId(null)
                .sedeId(null)
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

        Map<String, Object> claims = construirClaims(usuario);
        String jwtToken = jwtService.generateToken(claims, usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .role(usuario.getRole().name())
                .companyId(usuario.getCompany() != null ? usuario.getCompany().getId() : null)
                .sedeId(usuario.getSede() != null ? usuario.getSede().getId() : null)
                .mensaje("Inicio de sesión exitoso")
                .build();
    }

    private Map<String, Object> construirClaims(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", usuario.getId());
        claims.put("role", usuario.getRole().name());
        claims.put("company_id", usuario.getCompany() != null ? usuario.getCompany().getId() : null);
        claims.put("sede_id", usuario.getSede() != null ? usuario.getSede().getId() : null);
        return claims;
    }
}
