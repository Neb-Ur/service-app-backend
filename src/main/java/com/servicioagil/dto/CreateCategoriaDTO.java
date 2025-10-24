package com.servicioagil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoriaDTO {
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
    
    private Integer ordenVisualizacion;
    
    @Size(max = 7, message = "El color debe ser un código hexadecimal válido")
    private String colorHexadecimal;
    
    @Size(max = 50, message = "El icono no puede exceder los 50 caracteres")
    private String icono;
    
    private String creadoPor;
}