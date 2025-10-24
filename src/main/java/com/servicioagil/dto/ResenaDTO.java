package com.servicioagil.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    
    private Long id;
    private Double calificacion;
    private String comentario;
    private String respuestaTecnico;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Cliente que hizo la reseña
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private String clienteEmail;
    
    // Técnico reseñado
    private Long tecnicoId;
    private String tecnicoNombre;
    private String tecnicoApellido;
    private String tecnicoEmail;
    
    // Solicitud relacionada
    private Long solicitudId;
    private String solicitudTitulo;
    private String solicitudDescripcion;
}