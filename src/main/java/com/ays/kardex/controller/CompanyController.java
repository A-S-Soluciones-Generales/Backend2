package com.ays.kardex.controller;

import com.ays.kardex.dto.auth.UsuarioResponse;
import com.ays.kardex.dto.company.CompanyRequest;
import com.ays.kardex.dto.company.CompanyResponse;
import com.ays.kardex.dto.company.CompanyStatusRequest;
import com.ays.kardex.dto.company.CompanyUpdateRequest;
import com.ays.kardex.dto.sede.SedeResponse;
import com.ays.kardex.service.CompanyService;
import com.ays.kardex.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Empresas", description = "Gestión de compañías")
public class CompanyController {

    private final CompanyService companyService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Crear empresa", description = "Registra una nueva empresa con RUC único")
    public ResponseEntity<CompanyResponse> crear(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Actualizar empresa", description = "Modifica los datos fiscales o de contacto de una empresa")
    public ResponseEntity<CompanyResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody CompanyUpdateRequest request) {
        return ResponseEntity.ok(companyService.actualizar(id, request));
    }

    @GetMapping
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Buscar empresas", description = "Filtra empresas por nombre o RUC/ID fiscal")
    public ResponseEntity<Page<CompanyResponse>> buscar(@RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "tax_id", required = false) String taxId,
                                                        Pageable pageable) {
        return ResponseEntity.ok(companyService.buscar(name, taxId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Obtener empresa", description = "Recupera el detalle completo de una empresa")
    public ResponseEntity<CompanyResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.obtener(id));
    }

    @GetMapping("/{id}/sedes")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Listar sedes de una empresa", description = "Obtiene todas las sedes asociadas a la empresa")
    public ResponseEntity<List<SedeResponse>> listarSedes(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.listarSedes(id));
    }

    @GetMapping("/{id}/users")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Usuarios activos por empresa", description = "Lista los usuarios activos asociados a la empresa")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.listarUsuariosPorCompany(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(summary = "Actualizar estado de empresa", description = "Activa o inactiva una empresa y sus sedes")
    public ResponseEntity<CompanyResponse> actualizarEstado(@PathVariable Long id,
                                                            @Valid @RequestBody CompanyStatusRequest request) {
        return ResponseEntity.ok(companyService.actualizarEstado(id, request));
    }
}
