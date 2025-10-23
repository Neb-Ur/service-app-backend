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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "API", description = "Endpoints principales de la API")
public class ApiController {

    @GetMapping
    @Operation(summary = "Información de la API", description = "Retorna información general de la API")
    @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        log.info("GET /api - Obteniendo información de la API");
        
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Servicio Ágil Backend API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("description", "API REST para gestión de usuarios y servicios");
        apiInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        apiInfo.put("javaVersion", System.getProperty("java.version"));
        apiInfo.put("springBootVersion", "3.1.5");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("usuarios", "/api/usuarios");
        endpoints.put("health", "/api/health");
        endpoints.put("docs", "/swagger-ui.html");
        endpoints.put("openapi", "/v3/api-docs");
        apiInfo.put("endpoints", endpoints);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", apiInfo);
        response.put("message", "Bienvenido al API de Servicio Ágil");
        
        return ResponseEntity.ok(response);
    }
}

