package com.servicioagil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicioagil.dto.EmergenciaRequestDTO;
import com.servicioagil.dto.EmergenciaResponseDTO;
import com.servicioagil.dto.NotificacionRespuestaDTO;
import com.servicioagil.entity.EmergenciaNotificacion;
import com.servicioagil.service.EmergenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/emergencias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Emergencias", description = "API para gestión de emergencias en tiempo real")
public class EmergenciaController {

    private final EmergenciaService emergenciaService;

    @PostMapping
    @Operation(summary = "Crear emergencia", description = "Crea una nueva solicitud de emergencia y busca técnicos cercanos")
    public ResponseEntity<Map<String, Object>> crearEmergencia(@Valid @RequestBody EmergenciaRequestDTO request) {
        log.info("POST /api/emergencias - Creando emergencia para usuario: {}", request.getUsuarioId());

        try {
            EmergenciaResponseDTO emergencia = emergenciaService.crearEmergencia(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", emergencia);
            response.put("message", "Emergencia creada exitosamente. Buscando técnicos cercanos...");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creando emergencia", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear emergencia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/responder")
    @Operation(summary = "Responder notificación", description = "Permite al técnico aceptar o rechazar una emergencia")
    public ResponseEntity<Map<String, Object>> responderNotificacion(@Valid @RequestBody NotificacionRespuestaDTO respuesta) {
        log.info("POST /api/emergencias/responder - Técnico {} respondiendo notificación {}", 
                respuesta.getTecnicoId(), respuesta.getNotificacionId());

        try {
            EmergenciaResponseDTO emergencia = emergenciaService.procesarRespuestaTecnico(respuesta);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", emergencia);
            response.put("message", respuesta.getAceptar() ? 
                    "Emergencia aceptada exitosamente" : 
                    "Emergencia rechazada. Buscando otro técnico...");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error procesando respuesta de emergencia", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al procesar respuesta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estado de emergencia", description = "Obtiene el estado actual de una emergencia")
    public ResponseEntity<Map<String, Object>> obtenerEstadoEmergencia(@PathVariable Long id) {
        log.info("GET /api/emergencias/{} - Obteniendo estado de emergencia", id);

        try {
            EmergenciaResponseDTO emergencia = emergenciaService.obtenerEstadoEmergencia(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", emergencia);
            response.put("message", "Estado de emergencia obtenido exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error obteniendo estado de emergencia", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/tecnico/{tecnicoId}/pendientes")
    @Operation(summary = "Obtener notificaciones pendientes", description = "Obtiene las notificaciones de emergencia pendientes para un técnico")
    public ResponseEntity<Map<String, Object>> obtenerNotificacionesPendientes(@PathVariable Long tecnicoId) {
        log.info("GET /api/emergencias/tecnico/{}/pendientes - Obteniendo notificaciones pendientes", tecnicoId);

        try {
            List<EmergenciaNotificacion> notificaciones = emergenciaService.obtenerNotificacionesPendientes(tecnicoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", notificaciones);
            response.put("count", notificaciones.size());
            response.put("message", "Notificaciones obtenidas exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error obteniendo notificaciones pendientes", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener notificaciones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
