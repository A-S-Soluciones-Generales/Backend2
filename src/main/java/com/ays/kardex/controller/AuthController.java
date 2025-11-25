package com.ays.kardex.controller;

import com.ays.kardex.dto.auth.AuthResponse;
import com.ays.kardex.dto.auth.LoginRequest;
import com.ays.kardex.dto.auth.RegistroRequest;
import com.ays.kardex.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión de usuarios. No requieren autenticación.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema. El email debe ser único. Retorna un token JWT que puede usarse inmediatamente para acceder a endpoints protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                                "tipo": "Bearer",
                                                "id": 1,
                                                "email": "juan@email.com",
                                                "nombre": "Juan",
                                                "apellido": "Pérez",
                                                "role": "USER",
                                                "mensaje": "Usuario registrado exitosamente"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de registro inválidos o email ya registrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": 400,
                                                "mensaje": "El email ya está registrado",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con email y contraseña. Retorna un token JWT válido por 24 horas que debe incluirse en el header 'Authorization: Bearer {token}' para acceder a endpoints protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                                "tipo": "Bearer",
                                                "id": 1,
                                                "email": "juan@email.com",
                                                "nombre": "Juan",
                                                "apellido": "Pérez",
                                                "role": "USER",
                                                "mensaje": "Inicio de sesión exitoso"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": 400,
                                                "mensaje": "Email o contraseña incorrectos",
                                                "timestamp": "2024-01-15T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
