package com.servicioagil.dto;

import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTecnicoDTO {
    
    @Size(max = 1000, message = "La experiencia no puede exceder los 1000 caracteres")
    private String experiencia;
    
    @Size(max = 500, message = "Las certificaciones no pueden exceder los 500 caracteres")
    private String certificaciones;
    
    @Size(max = 1000, message = "Las habilidades no pueden exceder los 1000 caracteres")
    private String habilidades;
    
    @DecimalMin(value = "0.0", message = "La tarifa por hora debe ser mayor a 0")
    @DecimalMax(value = "9999.99", message = "La tarifa por hora no puede exceder 9999.99")
    private Double tarifaHora;
    
    @Size(max = 300, message = "La zona de cobertura no puede exceder los 300 caracteres")
    private String zonaCobertura;
    
    @Size(max = 500, message = "El horario de disponibilidad no puede exceder los 500 caracteres")
    private String horarioDisponibilidad;
    
    @Size(max = 1000, message = "La descripción personal no puede exceder los 1000 caracteres")
    private String descripcionPersonal;
    
    @Size(max = 200, message = "La URL del portfolio no puede exceder los 200 caracteres")
    private String urlPortfolio;
    
    private Boolean disponible = true;
    
    // ID de la empresa (opcional)
    private Long empresaId;
    
    // IDs de subcategorías en las que se especializa
    @NotNull(message = "Debe seleccionar al menos una subcategoría")
    private List<Long> subcategoriaIds;
}