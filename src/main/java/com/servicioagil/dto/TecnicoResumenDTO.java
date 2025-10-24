package com.servicioagil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoResumenDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Double promedioCalificacion;
    private Integer totalResenas;
    private String clasificacion;
    private Boolean disponible;
    private Boolean activo;
}