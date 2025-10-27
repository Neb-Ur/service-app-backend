package com.servicioagil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergenciaRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoriaId;

    @NotNull(message = "El ID de la subcategoría es obligatorio")
    private Long subcategoriaId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    private String telefono;

    private String notas;
}
