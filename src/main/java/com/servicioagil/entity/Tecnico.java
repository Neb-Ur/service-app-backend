package com.servicioagil.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tecnicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000, message = "La descripción profesional no puede exceder los 1000 caracteres")
    @Column(name = "descripcion_profesional", length = 1000)
    private String descripcionProfesional;

    @Min(value = 1, message = "La clasificación debe ser entre 1 y 5")
    @Max(value = 5, message = "La clasificación debe ser entre 1 y 5")
    @Column(name = "clasificacion")
    private Integer clasificacion;

    @DecimalMin(value = "0.0", message = "El rating debe ser mayor o igual a 0")
    @DecimalMax(value = "5.0", message = "El rating debe ser menor o igual a 5")
    @Column(name = "rating_promedio", precision = 3, scale = 2)
    private BigDecimal ratingPromedio = BigDecimal.ZERO;

    @Column(name = "total_resenas", nullable = false)
    private Integer totalResenas = 0;

    @Column(name = "total_trabajos_completados", nullable = false)
    private Integer totalTrabajosCompletados = 0;

    @Column(name = "precio_base_hora", precision = 10, scale = 2)
    private BigDecimal precioBaseHora;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    @Column(name = "verificado", nullable = false)
    private Boolean verificado = false;

    @Size(max = 500, message = "Los certificados no pueden exceder los 500 caracteres")
    @Column(name = "certificados", length = 500)
    private String certificados;

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    @Column(name = "radio_cobertura_km")
    private Integer radioCobertura;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // Relación con usuario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con empresa (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    // Relación con subcategoría (un técnico pertenece a una subcategoría)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategoria_id", nullable = false)
    private Subcategoria subcategoria;

    // Relación con reseñas recibidas
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resena> resenasRecibidas = new ArrayList<>();

    // Relación con solicitudes asignadas al técnico
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Solicitud> solicitudesAsignadas = new ArrayList<>();

    // Campos de auditoría
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "creado_por", length = 100)
    private String creadoPor;

    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;



    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (disponible == null) {
            disponible = true;
        }
        if (verificado == null) {
            verificado = false;
        }
        if (totalResenas == null) {
            totalResenas = 0;
        }
        if (totalTrabajosCompletados == null) {
            totalTrabajosCompletados = 0;
        }
        if (ratingPromedio == null) {
            ratingPromedio = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}