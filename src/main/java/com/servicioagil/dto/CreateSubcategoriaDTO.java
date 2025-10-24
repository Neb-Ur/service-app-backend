package com.servicioagil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubcategoriaDTO {
    
    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
    
    private Integer ordenVisualizacion;
    
    @Size(max = 7, message = "El color debe ser un código hexadecimal válido")
    private String colorHexadecimal;
    
    @Size(max = 50, message = "El icono no puede exceder los 50 caracteres")
    private String icono;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    // Campos de filtro opcionales
    private Boolean permiteUrgente = false;
    private Boolean incluyeMateriales = false;
    private Boolean emiteBoletaFactura = false;
    private Boolean ofreceGarantia = false;
    private Boolean trabajoInterior = false;
    private Boolean trabajoExterior = false;
    private Boolean permiteMetrosCuadrados = false;
    private Boolean permiteMetrosLineales = false;
    private Boolean trabajoEnAltura = false;
    private Boolean retiroEscombros = false;
    private Boolean atencionHoy = false;
    private Boolean atencionFinSemana = false;
    private Boolean atencionNocturna = false;
    private Boolean atencion24_7 = false;
    private Integer radioCobertura;
    private String tipoPrecio;
    private Double ratingMinimo;
    private Integer tiempoRespuestaHoras;
    private Boolean requiereCertificaciones = false;
    private String certificacionesDisponibles;
    private String marcasCompatibles;
    private Boolean incluyeVisitaTecnica = false;
    private Boolean diagnosticoSinCosto = false;
    private Boolean incluyeRepuestos = false;
    private Boolean servicioDomicilio = false;
    private Boolean servicioSalon = false;
    private Boolean paraEventos = false;
    private Boolean paraNovias = false;
    private Boolean esterilizacionInstrumentos = false;
    private Boolean insumosIncluidos = false;
    private Boolean productosVeganos = false;
    private Integer duracionServicioMinutos;
    private Boolean ofrecePacksCombos = false;
    private Boolean horarioExtendido = false;
    private String preferenciaProfesional;
    
    private String creadoPor;
}