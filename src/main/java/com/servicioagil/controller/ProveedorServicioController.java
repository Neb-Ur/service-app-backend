package com.servicioagil.controller;

import com.servicioagil.dto.ProveedorServicioDTO;
import com.servicioagil.service.ProveedorServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores de Servicio", description = "API para obtener proveedores de servicio (técnicos y empresas) por subcategoría")
public class ProveedorServicioController {

    private final ProveedorServicioService proveedorServicioService;

    @GetMapping("/subcategoria/{subcategoriaId}")
    @Operation(
        summary = "Obtener proveedores por subcategoría",
        description = "Retorna una lista unificada de técnicos independientes y empresas que ofrecen servicios en la subcategoría especificada. " +
                     "Incluye técnicos que trabajan de forma independiente y empresas que tienen al menos un técnico disponible en esa subcategoría."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada")
    })
    public ResponseEntity<List<ProveedorServicioDTO>> getProveedoresPorSubcategoria(
            @Parameter(description = "ID de la subcategoría", required = true, example = "1")
            @PathVariable Long subcategoriaId) {
        
        List<ProveedorServicioDTO> proveedores = proveedorServicioService.getProveedoresPorSubcategoria(subcategoriaId);
        return ResponseEntity.ok(proveedores);
    }
}
