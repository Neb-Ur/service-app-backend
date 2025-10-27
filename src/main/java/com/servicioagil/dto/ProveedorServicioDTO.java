package com.servicioagil.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorServicioDTO {
    private Long id;
    private String nombre;
    private Boolean esEmpresa;
    private BigDecimal precioBase;
    private String foto;
    private String descripcion;
    private String informacionAdicional;
    private Double calificacionPromedio;
    private Integer numeroResenas;
    private String telefono;
    private String email;
    private String direccion;
    private Boolean disponible;
    private String subcategoriaNombre;
    private Long subcategoriaId;
}
