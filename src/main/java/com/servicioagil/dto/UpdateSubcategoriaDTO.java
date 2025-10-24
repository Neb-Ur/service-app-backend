package com.servicioagil.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubcategoriaDTO {
    
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    private Boolean activo;
    
    private Integer ordenVisualizacion;
    
    @Size(max = 7, message = "El color debe ser un código hexadecimal válido")
    private String colorHexadecimal;
    
    @Size(max = 50, message = "El icono no puede exceder los 50 caracteres")
    private String icono;
    
    private Long categoriaId;
    
    // Campos de filtro opcionales para actualizar
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
    
    private String modificadoPor;
}