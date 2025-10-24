package com.servicioagil.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDTO {
    
    private Long id;
    private String razonSocial;
    private String nombreComercial;
    private String rfc;
    private String telefono;
    private String email;
    private String direccion;
    private String sitioWeb;
    private String descripcion;
    private String sector;
    private Integer numeroEmpleados;
    private String contactoPrincipal;
    private String telefonoContacto;
    private String emailContacto;
    private Boolean verificada;
    private LocalDateTime fechaVerificacion;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Usuario propietario
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    
    // Lista de técnicos
    private List<TecnicoResumenDTO> tecnicos;
}