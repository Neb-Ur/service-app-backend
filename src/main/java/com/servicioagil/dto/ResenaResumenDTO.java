package com.servicioagil.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaResumenDTO {
    
    private Long id;
    private Double calificacion;
    private String comentario;
    private LocalDateTime fechaCreacion;
    
    // Cliente que hizo la reseña
    private String clienteNombre;
    private String clienteApellido;
    
    // Solicitud relacionada
    private Long solicitudId;
    private String solicitudDescripcion;
}