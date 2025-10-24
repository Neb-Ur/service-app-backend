package com.servicioagil.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDTO {
    
    private Long id;
    private String experiencia;
    private String certificaciones;
    private String habilidades;
    private Double tarifaHora;
    private String zonaCobertura;
    private String horarioDisponibilidad;
    private Double promedioCalificacion;
    private Integer totalResenas;
    private String clasificacion;
    private String descripcionPersonal;
    private String urlPortfolio;
    private Boolean disponible;
    private Boolean verificado;
    private LocalDateTime fechaVerificacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Usuario asociado
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApellido;
    private String usuarioEmail;
    private String usuarioTelefono;
    
    // Empresa (si pertenece a una)
    private Long empresaId;
    private String empresaNombre;
    
    // Subcategorías especializadas
    private List<SubcategoriaDTO> subcategorias;
    
    // Reseñas recientes (últimas 5)
    private List<ResenaResumenDTO> resenasRecientes;
}