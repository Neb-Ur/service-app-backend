package com.servicioagil.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSuscripcionDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 5000, message = "La descripción no puede exceder 5000 caracteres")
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio original debe ser mayor que 0")
    @Digits(integer = 8, fraction = 2, message = "El precio original debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precioOriginal;

    @DecimalMin(value = "0.0", message = "El porcentaje de descuento debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje de descuento no puede exceder 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje de descuento debe tener máximo 3 dígitos enteros y 2 decimales")
    private BigDecimal porcentajeDescuento;

    @Min(value = 1, message = "La duración debe ser al menos 1 día")
    private Integer duracionDias;

    @Min(value = 0, message = "El número de solicitudes incluidas debe ser mayor o igual a 0")
    private Integer numeroSolicitudesIncluidas;

    private Boolean soportePrioritario;

    private Boolean destacado;

    private Boolean activo;

    @Min(value = 0, message = "El orden de visualización debe ser mayor o igual a 0")
    private Integer ordenVisualizacion;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "El color debe ser un código hexadecimal válido (ej: #FF5733)")
    private String colorHexadecimal;

    @Size(max = 50, message = "El icono no puede exceder 50 caracteres")
    private String icono;

    private String modificadoPor;
}
