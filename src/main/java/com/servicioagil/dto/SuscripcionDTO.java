package com.servicioagil.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioOriginal;
    private BigDecimal porcentajeDescuento;
    private BigDecimal precioFinal;
    private Integer duracionDias;
    private Integer numeroSolicitudesIncluidas;
    private Boolean soportePrioritario;
    private Boolean destacado;
    private Boolean activo;
    private Integer ordenVisualizacion;
    private String colorHexadecimal;
    private String icono;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private String creadoPor;
    private String modificadoPor;
}
