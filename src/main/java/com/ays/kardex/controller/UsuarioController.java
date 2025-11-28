package com.ays.kardex.controller;

import com.ays.kardex.dto.auth.UsuarioCreateRequest;
import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.auth.UsuarioUpdateRequest;
import com.ays.kardex.entity.Usuario;
import com.ays.kardex.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

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
                .documento(usuario.getDocumento())
                .role(usuario.getRole().name())
                .companyId(usuario.getCompany() != null ? usuario.getCompany().getId() : null)
                .sedeId(usuario.getSede() != null ? usuario.getSede().getId() : null)
                .sedeNombre(usuario.getSede() != null ? usuario.getSede().getNombre() : null)
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Crear usuario", description = "Permite a un Global Admin registrar administradores globales o vendedores")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioResponse response = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Actualizar usuario", description = "Permite modificar datos básicos del usuario")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable Long id,
                                                             @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Inactivar usuario", description = "Realiza un borrado lógico del usuario")
    public ResponseEntity<Void> inactivarUsuario(@PathVariable Long id) {
        usuarioService.inactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Buscar usuarios", description = "Busca por nombre, email o documento")
    public ResponseEntity<List<UsuarioResponse>> buscarUsuarios(@RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(search));
    }
}
