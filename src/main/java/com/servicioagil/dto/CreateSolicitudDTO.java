package com.servicioagil.dto;

import java.time.LocalDateTime;

import com.servicioagil.entity.Solicitud;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSolicitudDTO {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 200, message = "El título debe tener entre 5 y 200 caracteres")
    private String titulo;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 2000, message = "La descripción debe tener entre 10 y 2000 caracteres")
    private String descripcion;
    
    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 500, message = "La ubicación no puede exceder los 500 caracteres")
    private String ubicacion;
    
    @DecimalMin(value = "0.0", message = "El presupuesto estimado debe ser mayor a 0")
    @DecimalMax(value = "999999.99", message = "El presupuesto estimado no puede exceder 999999.99")
    private Double presupuestoEstimado;
    
    private LocalDateTime fechaRequerida;
    
    private Solicitud.PrioridadSolicitud prioridad = Solicitud.PrioridadSolicitud.NORMAL;
    
    @Size(max = 1000, message = "Las notas no pueden exceder los 1000 caracteres")
    private String notas;
    
    @NotNull(message = "Debe seleccionar una subcategoría")
    private Long subcategoriaId;
    
    // ID del técnico (opcional, para asignar directamente)
    private Long tecnicoId;
}