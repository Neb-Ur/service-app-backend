package com.servicioagil.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergenciaResponseDTO {

    private Long solicitudId;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private String descripcion;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private TecnicoEmergenciaDTO tecnicoAsignado;
    private List<NotificacionDTO> notificaciones;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TecnicoEmergenciaDTO {
        private Long id;
        private String nombre;
        private String apellido;
        private String telefono;
        private Double latitud;
        private Double longitud;
        private Double distanciaMetros;
        private Integer tiempoEstimadoMinutos;
        private String estado;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificacionDTO {
        private Long id;
        private Long tecnicoId;
        private String tecnicoNombre;
        private String estado;
        private LocalDateTime fechaEnvio;
        private LocalDateTime fechaRespuesta;
        private LocalDateTime timeoutEn;
        private Double distanciaMetros;
        private Integer ordenContacto;
    }
}
