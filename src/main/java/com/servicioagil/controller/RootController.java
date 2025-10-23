package com.servicioagil.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
public class RootController {

    @GetMapping
    @Operation(summary = "Página de inicio", description = "Endpoint raíz de la aplicación")
    @ApiResponse(responseCode = "200", description = "Página de inicio cargada exitosamente")
    public ResponseEntity<Map<String, Object>> home() {
        log.info("GET / - Acceso a página de inicio");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Bienvenido al API de Servicio Ágil v1.0.0");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "ACTIVO");
        response.put("documentation", "/swagger-ui.html");
        response.put("apiEndpoint", "/api");
        
        return ResponseEntity.ok(response);
    }
}