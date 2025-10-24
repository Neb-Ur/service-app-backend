package com.servicioagil.dto;

import java.time.LocalDateTime;

import com.servicioagil.entity.Solicitud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDTO {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double presupuestoEstimado;
    private LocalDateTime fechaRequerida;
    private Solicitud.EstadoSolicitud estado;
    private Solicitud.PrioridadSolicitud prioridad;
    private String notas;
    private Double costoFinal;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteApellido;
    private String clienteEmail;
    private String clienteTelefono;
    
    // Técnico asignado
    private Long tecnicoId;
    private String tecnicoNombre;
    private String tecnicoApellido;
    private String tecnicoEmail;
    private String tecnicoTelefono;
    
    // Subcategoría
    private Long subcategoriaId;
    private String subcategoriaNombre;
    private String categoriaNombre;
    
    // Reseña (si existe)
    private ResenaDTO resena;
}