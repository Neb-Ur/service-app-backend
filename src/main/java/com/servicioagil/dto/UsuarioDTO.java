package com.servicioagil.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.servicioagil.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaNacimiento;
    
    private Usuario.TipoUsuario tipoUsuario;
    private Boolean activo;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;
    
    // Información adicional según el tipo de usuario
    private Long empresaId;
    private String empresaNombre;
    private Long tecnicoId;
    private Integer tecnicoClasificacion;
    private Double tecnicoRating;
    
    // Estadísticas
    private Long totalSolicitudes;
    private Long totalResenasEscritas;
}