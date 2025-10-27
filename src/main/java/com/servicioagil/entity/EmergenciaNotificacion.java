package com.servicioagil.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "emergencia_notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergenciaNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Tecnico tecnico;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoNotificacion estado = EstadoNotificacion.PENDIENTE;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    @Column(name = "timeout_en", nullable = false)
    private LocalDateTime timeoutEn;

    @Column(name = "distancia_metros")
    private Double distanciaMetros;

    @Column(name = "orden_contacto")
    private Integer ordenContacto;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
        fechaEnvio = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoNotificacion.PENDIENTE;
        }
        // Timeout de 90 segundos por defecto
        if (timeoutEn == null) {
            timeoutEn = LocalDateTime.now().plusSeconds(90);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    public enum EstadoNotificacion {
        PENDIENTE,      // Notificación enviada, esperando respuesta
        ACEPTADA,       // Técnico aceptó la emergencia
        RECHAZADA,      // Técnico rechazó la emergencia
        TIMEOUT,        // Tiempo de respuesta agotado
        CANCELADA       // Emergencia cancelada antes de respuesta
    }
}
