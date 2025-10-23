package com.servicioagil.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/health")
@Slf4j
@Tag(name = "Health", description = "API para verificación del estado del sistema")
public class HealthController implements HealthIndicator {

    @GetMapping
    @Operation(summary = "Verificar estado del sistema", description = "Retorna el estado actual del sistema")
    @ApiResponse(responseCode = "200", description = "Estado del sistema obtenido exitosamente")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        log.info("GET /api/health - Verificando estado del sistema");
        
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        healthInfo.put("uptime", getUptime());
        healthInfo.put("version", "1.0.0");
        healthInfo.put("environment", System.getProperty("spring.profiles.active", "development"));
        
        Map<String, String> services = new HashMap<>();
        services.put("database", "UP");
        services.put("api", "UP");
        healthInfo.put("services", services);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", healthInfo);
        response.put("message", "Sistema funcionando correctamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/system")
    @Operation(summary = "Obtener información del sistema", description = "Retorna información detallada del sistema")
    @ApiResponse(responseCode = "200", description = "Información del sistema obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        log.info("GET /api/health/system - Obteniendo información del sistema");
        
        Runtime runtime = Runtime.getRuntime();
        
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("javaVendor", System.getProperty("java.vendor"));
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("availableProcessors", runtime.availableProcessors());
        systemInfo.put("totalMemory", runtime.totalMemory());
        systemInfo.put("freeMemory", runtime.freeMemory());
        systemInfo.put("maxMemory", runtime.maxMemory());
        systemInfo.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", systemInfo);
        response.put("message", "Información del sistema obtenida exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @Override
    public Health health() {
        // Verificaciones de salud personalizadas
        try {
            // Aquí podrías agregar verificaciones adicionales
            // como conectividad a la base de datos, servicios externos, etc.
            
            return Health.up()
                    .withDetail("status", "Sistema funcionando correctamente")
                    .withDetail("timestamp", LocalDateTime.now())
                    .withDetail("uptime", getUptime())
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now())
                    .build();
        }
    }

    private String getUptime() {
        long uptimeMillis = System.currentTimeMillis() - 
                           java.lang.management.ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptimeSeconds = uptimeMillis / 1000;
        long hours = uptimeSeconds / 3600;
        long minutes = (uptimeSeconds % 3600) / 60;
        long seconds = uptimeSeconds % 60;
        
        return String.format("%d horas, %d minutos, %d segundos", hours, minutes, seconds);
    }
}