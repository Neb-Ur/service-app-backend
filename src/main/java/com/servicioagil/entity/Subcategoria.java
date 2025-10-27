package com.servicioagil.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subcategorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @NotNull(message = "El estado activo es obligatorio")
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;

    @Column(name = "color_hexadecimal", length = 7)
    private String colorHexadecimal;

    @Column(name = "icono", length = 50)
    private String icono;

    // Campos de filtro específicos
    @Column(name = "permite_urgente")
    private Boolean permiteUrgente = false;

    @Column(name = "incluye_materiales")
    private Boolean incluyeMateriales = false;

    @Column(name = "emite_boleta_factura")
    private Boolean emiteBoletaFactura = false;

    @Column(name = "ofrece_garantia")
    private Boolean ofreceGarantia = false;

    @Column(name = "trabajo_interior")
    private Boolean trabajoInterior = false;

    @Column(name = "trabajo_exterior")
    private Boolean trabajoExterior = false;

    @Column(name = "permite_metros_cuadrados")
    private Boolean permiteMetrosCuadrados = false;

    @Column(name = "permite_metros_lineales")
    private Boolean permiteMetrosLineales = false;

    @Column(name = "trabajo_en_altura")
    private Boolean trabajoEnAltura = false;

    @Column(name = "retiro_escombros")
    private Boolean retiroEscombros = false;

    @Column(name = "atencion_hoy")
    private Boolean atencionHoy = false;

    @Column(name = "atencion_fin_semana")
    private Boolean atencionFinSemana = false;

    @Column(name = "atencion_nocturna")
    private Boolean atencionNocturna = false;

    @Column(name = "atencion_24_7")
    private Boolean atencion24_7 = false;

    @Column(name = "radio_cobertura_km")
    private Integer radioCobertura;

    @Column(name = "precio_tipo")
    @Enumerated(EnumType.STRING)
    private TipoPrecio tipoPrecio;

    @Column(name = "requiere_rating_minimo")
    private Double ratingMinimo;

    @Column(name = "tiempo_respuesta_horas")
    private Integer tiempoRespuestaHoras;

    @Column(name = "requiere_certificaciones")
    private Boolean requiereCertificaciones = false;

    @Column(name = "certificaciones_disponibles", length = 500)
    private String certificacionesDisponibles;

    @Column(name = "marcas_compatibles", length = 500)
    private String marcasCompatibles;

    @Column(name = "incluye_visita_tecnica")
    private Boolean incluyeVisitaTecnica = false;

    @Column(name = "diagnostico_sin_costo")
    private Boolean diagnosticoSinCosto = false;

    @Column(name = "incluye_repuestos")
    private Boolean incluyeRepuestos = false;

    @Column(name = "servicio_domicilio")
    private Boolean servicioDomicilio = false;

    @Column(name = "servicio_salon")
    private Boolean servicioSalon = false;

    @Column(name = "para_eventos")
    private Boolean paraEventos = false;

    @Column(name = "para_novias")
    private Boolean paraNovias = false;

    @Column(name = "esterilizacion_instrumentos")
    private Boolean esterilizacionInstrumentos = false;

    @Column(name = "insumos_incluidos")
    private Boolean insumosIncluidos = false;

    @Column(name = "productos_veganos")
    private Boolean productosVeganos = false;

    @Column(name = "duracion_servicio_minutos")
    private Integer duracionServicioMinutos;

    @Column(name = "ofrece_packs_combos")
    private Boolean ofrecePacksCombos = false;

    @Column(name = "horario_extendido")
    private Boolean horarioExtendido = false;

    @Column(name = "preferencia_profesional")
    @Enumerated(EnumType.STRING)
    private PreferenciaProfesional preferenciaProfesional;

    // Relación con categoría padre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relación con técnicos (una subcategoría puede tener múltiples técnicos)
    @OneToMany(mappedBy = "subcategoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tecnico> tecnicos = new ArrayList<>();

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
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }

    public enum TipoPrecio {
        FIJO,
        POR_HORA,
        NEGOCIABLE,
        COTIZACION
    }

    public enum PreferenciaProfesional {
        SIN_PREFERENCIA,
        MUJER,
        HOMBRE
    }
}