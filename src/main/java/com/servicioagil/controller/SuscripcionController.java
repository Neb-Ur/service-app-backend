package com.servicioagil.controller;

import com.servicioagil.dto.CreateSuscripcionDTO;
import com.servicioagil.dto.SuscripcionDTO;
import com.servicioagil.dto.UpdateSuscripcionDTO;
import com.servicioagil.service.SuscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
@Tag(name = "Suscripciones", description = "API para la gestión de suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    @GetMapping
    @Operation(summary = "Obtener todas las suscripciones", description = "Retorna una lista con todas las suscripciones registradas")
    @ApiResponse(responseCode = "200", description = "Lista de suscripciones obtenida exitosamente")
    public ResponseEntity<List<SuscripcionDTO>> getAllSuscripciones() {
        List<SuscripcionDTO> suscripciones = suscripcionService.getAllSuscripciones();
        return ResponseEntity.ok(suscripciones);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener suscripciones activas", description = "Retorna una lista con todas las suscripciones activas ordenadas por visualización")
    @ApiResponse(responseCode = "200", description = "Lista de suscripciones activas obtenida exitosamente")
    public ResponseEntity<List<SuscripcionDTO>> getSuscripcionesActivas() {
        List<SuscripcionDTO> suscripciones = suscripcionService.getSuscripcionesActivas();
        return ResponseEntity.ok(suscripciones);
    }

    @GetMapping("/destacadas")
    @Operation(summary = "Obtener suscripciones destacadas", description = "Retorna una lista con las suscripciones destacadas y activas")
    @ApiResponse(responseCode = "200", description = "Lista de suscripciones destacadas obtenida exitosamente")
    public ResponseEntity<List<SuscripcionDTO>> getSuscripcionesDestacadas() {
        List<SuscripcionDTO> suscripciones = suscripcionService.getSuscripcionesDestacadas();
        return ResponseEntity.ok(suscripciones);
    }

    @GetMapping("/con-descuento")
    @Operation(summary = "Obtener suscripciones con descuento", description = "Retorna una lista con las suscripciones que tienen descuento aplicado")
    @ApiResponse(responseCode = "200", description = "Lista de suscripciones con descuento obtenida exitosamente")
    public ResponseEntity<List<SuscripcionDTO>> getSuscripcionesConDescuento() {
        List<SuscripcionDTO> suscripciones = suscripcionService.getSuscripcionesConDescuento();
        return ResponseEntity.ok(suscripciones);
    }

    @GetMapping("/rango-precio")
    @Operation(summary = "Buscar suscripciones por rango de precio", description = "Retorna suscripciones cuyo precio final esté dentro del rango especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripciones encontradas"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<List<SuscripcionDTO>> getSuscripcionesPorRangoPrecio(
            @Parameter(description = "Precio mínimo", required = true)
            @RequestParam BigDecimal minPrecio,
            @Parameter(description = "Precio máximo", required = true)
            @RequestParam BigDecimal maxPrecio) {
        List<SuscripcionDTO> suscripciones = suscripcionService.getSuscripcionesPorRangoPrecio(minPrecio, maxPrecio);
        return ResponseEntity.ok(suscripciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener suscripción por ID", description = "Retorna los detalles de una suscripción específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    public ResponseEntity<SuscripcionDTO> getSuscripcionById(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable Long id) {
        SuscripcionDTO suscripcion = suscripcionService.getSuscripcionById(id);
        return ResponseEntity.ok(suscripcion);
    }

    @PostMapping
    @Operation(summary = "Crear nueva suscripción", description = "Crea una nueva suscripción con los datos proporcionados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Suscripción creada exitosamente",
                    content = @Content(schema = @Schema(implementation = SuscripcionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<SuscripcionDTO> createSuscripcion(
            @Parameter(description = "Datos de la nueva suscripción", required = true)
            @Valid @RequestBody CreateSuscripcionDTO createDTO) {
        SuscripcionDTO nuevaSuscripcion = suscripcionService.createSuscripcion(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSuscripcion);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar suscripción", description = "Actualiza los datos de una suscripción existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Suscripción actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<SuscripcionDTO> updateSuscripcion(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados de la suscripción", required = true)
            @Valid @RequestBody UpdateSuscripcionDTO updateDTO) {
        SuscripcionDTO suscripcionActualizada = suscripcionService.updateSuscripcion(id, updateDTO);
        return ResponseEntity.ok(suscripcionActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar suscripción", description = "Elimina permanentemente una suscripción")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Suscripción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    public ResponseEntity<Void> deleteSuscripcion(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable Long id) {
        suscripcionService.deleteSuscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-activo")
    @Operation(summary = "Cambiar estado activo", description = "Activa o desactiva una suscripción")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    public ResponseEntity<SuscripcionDTO> toggleActivoSuscripcion(
            @Parameter(description = "ID de la suscripción", required = true)
            @PathVariable Long id) {
        SuscripcionDTO suscripcion = suscripcionService.toggleActivoSuscripcion(id);
        return ResponseEntity.ok(suscripcion);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de suscripciones", description = "Retorna estadísticas generales sobre las suscripciones")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalSuscripciones", suscripcionService.getAllSuscripciones().size());
        estadisticas.put("suscripcionesActivas", suscripcionService.contarSuscripcionesActivas());
        estadisticas.put("suscripcionesDestacadas", suscripcionService.getSuscripcionesDestacadas().size());
        estadisticas.put("suscripcionesConDescuento", suscripcionService.getSuscripcionesConDescuento().size());
        return ResponseEntity.ok(estadisticas);
    }
}
