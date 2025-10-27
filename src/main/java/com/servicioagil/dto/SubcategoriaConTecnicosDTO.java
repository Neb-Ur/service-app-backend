package com.servicioagil.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoriaConTecnicosDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Integer ordenVisualizacion;
    private String colorHexadecimal;
    private String icono;
    private Long categoriaId;
    private String categoriaNombre;
    private List<TecnicoResumenDTO> tecnicos;
}
