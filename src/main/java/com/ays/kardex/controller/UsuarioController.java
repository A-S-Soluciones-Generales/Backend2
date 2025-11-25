package com.ays.kardex.controller;

import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    @GetMapping("/me")
    @Operation(
            summary = "Obtener usuario actual",
            description = "Retorna los datos del usuario autenticado basándose en el token JWT"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos del usuario obtenidos exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado - Token inválido o expirado"
            )
    })
    public ResponseEntity<UsuarioResponse> obtenerUsuarioActual(@AuthenticationPrincipal Usuario usuario) {
        UsuarioResponse response = UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .role(usuario.getRole().name())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();

        return ResponseEntity.ok(response);
    }
}
