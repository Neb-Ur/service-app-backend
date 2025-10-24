package com.servicioagil.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicioagil.dto.CreateEmpresaDTO;
import com.servicioagil.dto.EmpresaDTO;
import com.servicioagil.dto.UpdateEmpresaDTO;
import com.servicioagil.service.EmpresaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
@Tag(name = "Empresas", description = "API para gestión de empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    @Operation(summary = "Obtener todas las empresas", description = "Obtiene una lista paginada de todas las empresas")
    public ResponseEntity<Page<EmpresaDTO>> getAllEmpresas(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<EmpresaDTO> empresas = empresaService.findWithFilters(null, null, null, pageable);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar empresas con filtros", description = "Busca empresas aplicando filtros opcionales")
    public ResponseEntity<Page<EmpresaDTO>> searchEmpresas(
            @Parameter(description = "Nombre de la empresa") @RequestParam(required = false) String nombre,
            @Parameter(description = "RUT/RFC de la empresa") @RequestParam(required = false) String rut,
            @Parameter(description = "Estado activo") @RequestParam(required = false) Boolean activo,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo de ordenamiento") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento") @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<EmpresaDTO> empresas = empresaService.findWithFilters(nombre, rut, activo, pageable);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empresa por ID", description = "Obtiene los detalles de una empresa específica")
    public ResponseEntity<EmpresaDTO> getEmpresaById(
            @Parameter(description = "ID de la empresa") @PathVariable Long id) {
        EmpresaDTO empresa = empresaService.findById(id);
        return ResponseEntity.ok(empresa);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener empresa por usuario", description = "Obtiene la empresa asociada a un usuario")
    public ResponseEntity<EmpresaDTO> getEmpresaByUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {
        EmpresaDTO empresa = empresaService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(empresa);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener empresas activas", description = "Obtiene todas las empresas activas")
    public ResponseEntity<List<EmpresaDTO>> getEmpresasActivas() {
        List<EmpresaDTO> empresas = empresaService.findByActiva(true);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Crear nueva empresa", description = "Crea una nueva empresa para un usuario")
    public ResponseEntity<EmpresaDTO> createEmpresa(
            @Parameter(description = "ID del usuario propietario") @PathVariable Long usuarioId,
            @Valid @RequestBody CreateEmpresaDTO createEmpresaDTO) {
        EmpresaDTO nuevaEmpresa = empresaService.create(createEmpresaDTO, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEmpresa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empresa", description = "Actualiza los datos de una empresa existente")
    public ResponseEntity<EmpresaDTO> updateEmpresa(
            @Parameter(description = "ID de la empresa") @PathVariable Long id,
            @Valid @RequestBody UpdateEmpresaDTO updateEmpresaDTO) {
        EmpresaDTO empresaActualizada = empresaService.update(id, updateEmpresaDTO);
        return ResponseEntity.ok(empresaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar empresa", description = "Marca una empresa como inactiva (eliminación lógica)")
    public ResponseEntity<Void> deleteEmpresa(
            @Parameter(description = "ID de la empresa") @PathVariable Long id) {
        empresaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}