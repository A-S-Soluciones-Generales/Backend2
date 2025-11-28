package com.ays.kardex.controller;

import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.sede.SedeRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.dto.sede.SedeUpdateRequest;
import com.ays.kardex.service.SedeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
@Tag(name = "Sedes", description = "Gestión de sedes por empresa")
public class SedeController {

    private final SedeService sedeService;

    @PostMapping
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Crear sede", description = "Crea una sede asociada a una empresa existente")
    public ResponseEntity<SedeResponse> crear(@Valid @RequestBody SedeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sedeService.crear(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GLOBAL_ADMIN', 'VENDEDOR')")
    @Operation(summary = "Obtener sede", description = "Recupera el detalle de una sede por su identificador")
    public ResponseEntity<SedeResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.obtener(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Actualizar sede", description = "Modifica el nombre o dirección de una sede existente")
    public ResponseEntity<SedeResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody SedeUpdateRequest request) {
        return ResponseEntity.ok(sedeService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Inactivar sede", description = "Inactiva una sede si no tiene stock disponible")
    public ResponseEntity<Void> inactivar(@PathVariable Long id) {
        sedeService.inactivar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/users")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Usuarios activos por sede", description = "Lista los usuarios con acceso activo a una sede específica")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosActivos(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.listarUsuariosActivos(id));
    }
}
