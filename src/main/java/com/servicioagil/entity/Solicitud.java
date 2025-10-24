package com.servicioagil.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título de la solicitud es obligatorio")
    @Size(min = 5, max = 200, message = "El título debe tener entre 5 y 200 caracteres")
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Size(max = 2000, message = "La descripción no puede exceder los 2000 caracteres")
    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    @Column(name = "direccion_servicio", length = 500)
    private String direccionServicio;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_preferida_inicio")
    private LocalDateTime fechaPreferidaInicio;

    @Column(name = "fecha_preferida_fin")
    private LocalDateTime fechaPreferidaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false, length = 15)
    private PrioridadSolicitud prioridad = PrioridadSolicitud.NORMAL;

    @Column(name = "presupuesto_estimado", precision = 10, scale = 2)
    private BigDecimal presupuestoEstimado;

    @Column(name = "precio_final", precision = 10, scale = 2)
    private BigDecimal precioFinal;

    @Column(name = "es_urgente", nullable = false)
    private Boolean esUrgente = false;

    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres")
    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "fecha_inicio_trabajo")
    private LocalDateTime fechaInicioTrabajo;

    @Column(name = "fecha_fin_trabajo")
    private LocalDateTime fechaFinTrabajo;

    // Relación con usuario que solicita el servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con técnico asignado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    // Relación con subcategoría del servicio solicitado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private Subcategoria subcategoria;

    // Relación con reseña (si existe)
    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Resena resena;

    // Campos de auditoría
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
        fechaSolicitud = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoSolicitud.PENDIENTE;
        }
        if (prioridad == null) {
            prioridad = PrioridadSolicitud.NORMAL;
        }
        if (esUrgente == null) {
            esUrgente = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    public enum EstadoSolicitud {
        PENDIENTE,      // Solicitud creada, esperando asignación
        ASIGNADA,       // Técnico asignado
        EN_PROGRESO,    // Trabajo iniciado
        COMPLETADA,     // Trabajo finalizado
        CANCELADA,      // Solicitud cancelada
        RECHAZADA       // Solicitud rechazada por técnico
    }

    public enum PrioridadSolicitud {
        BAJA,
        NORMAL,
        ALTA,
        URGENTE
    }
}