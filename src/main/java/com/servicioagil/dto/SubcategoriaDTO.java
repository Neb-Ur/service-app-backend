package com.servicioagil.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoriaDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private Integer ordenVisualizacion;
    private String colorHexadecimal;
    private String icono;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaModificacion;
    
    private String creadoPor;
    private String modificadoPor;
    
    // Información de la categoría padre
    private Long categoriaId;
    private String categoriaNombre;
    
    // Campos de filtro
    private Boolean permiteUrgente;
    private Boolean incluyeMateriales;
    private Boolean emiteBoletaFactura;
    private Boolean ofreceGarantia;
    private Boolean trabajoInterior;
    private Boolean trabajoExterior;
    private Boolean permiteMetrosCuadrados;
    private Boolean permiteMetrosLineales;
    private Boolean trabajoEnAltura;
    private Boolean retiroEscombros;
    private Boolean atencionHoy;
    private Boolean atencionFinSemana;
    private Boolean atencionNocturna;
    private Boolean atencion24_7;
    private Integer radioCobertura;
    private String tipoPrecio;
    private Double ratingMinimo;
    private Integer tiempoRespuestaHoras;
    private Boolean requiereCertificaciones;
    private String certificacionesDisponibles;
    private String marcasCompatibles;
    private Boolean incluyeVisitaTecnica;
    private Boolean diagnosticoSinCosto;
    private Boolean incluyeRepuestos;
    private Boolean servicioDomicilio;
    private Boolean servicioSalon;
    private Boolean paraEventos;
    private Boolean paraNovias;
    private Boolean esterilizacionInstrumentos;
    private Boolean insumosIncluidos;
    private Boolean productosVeganos;
    private Integer duracionServicioMinutos;
    private Boolean ofrecePacksCombos;
    private Boolean horarioExtendido;
    private String preferenciaProfesional;
}