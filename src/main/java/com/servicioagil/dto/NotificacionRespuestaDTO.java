package com.servicioagil.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRespuestaDTO {

    @NotNull(message = "El ID de la notificación es obligatorio")
    private Long notificacionId;

    @NotNull(message = "El ID del técnico es obligatorio")
    private Long tecnicoId;

    @NotNull(message = "La respuesta es obligatoria (true = aceptar, false = rechazar)")
    private Boolean aceptar;

    private Double latitudActual;
    private Double longitudActual;
}
