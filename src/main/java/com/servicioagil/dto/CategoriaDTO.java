package com.servicioagil.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Integer ordenVisualizacion;
    private String colorHexadecimal;
    private String icono;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaModificacion;
    
    private String creadoPor;
    private String modificadoPor;
    
    // Lista de subcategorías (solo cuando se necesite el detalle completo)
    private List<SubcategoriaDTO> subcategorias;
    
    // Contador de subcategorías activas (para listados)
    private Long totalSubcategorias;
}