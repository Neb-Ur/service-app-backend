package com.servicioagil.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "suscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_original", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioOriginal;

    @Column(name = "porcentaje_descuento", precision = 5, scale = 2)
    private BigDecimal porcentajeDescuento;

    @Column(name = "precio_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioFinal;

    @Column(name = "duracion_dias", nullable = false)
    private Integer duracionDias;

    @Column(name = "numero_solicitudes_incluidas")
    private Integer numeroSolicitudesIncluidas;

    @Column(name = "soporte_prioritario")
    private Boolean soportePrioritario = false;

    @Column(name = "destacado")
    private Boolean destacado = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;

    @Column(name = "color_hexadecimal", length = 7)
    private String colorHexadecimal;

    @Column(length = 50)
    private String icono;

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
        if (soportePrioritario == null) {
            soportePrioritario = false;
        }
        if (destacado == null) {
            destacado = false;
        }
        calcularPrecioFinal();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
        calcularPrecioFinal();
    }

    private void calcularPrecioFinal() {
        if (precioOriginal != null) {
            if (porcentajeDescuento != null && porcentajeDescuento.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal descuento = precioOriginal.multiply(porcentajeDescuento).divide(new BigDecimal("100"));
                precioFinal = precioOriginal.subtract(descuento);
            } else {
                precioFinal = precioOriginal;
            }
        }
    }
}
