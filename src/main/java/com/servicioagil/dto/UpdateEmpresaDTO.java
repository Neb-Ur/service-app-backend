package com.servicioagil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmpresaDTO {
    
    @Size(min = 2, max = 200, message = "La razón social debe tener entre 2 y 200 caracteres")
    private String razonSocial;
    
    @Size(max = 200, message = "El nombre comercial no puede exceder los 200 caracteres")
    private String nombreComercial;
    
    @Pattern(regexp = "^[A-Z&Ñ]{3,4}[0-9]{6}[A-Z0-9]{3}$", message = "El RFC debe tener un formato válido")
    private String rfc;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El teléfono debe tener un formato válido")
    private String telefono;
    
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    private String direccion;
    
    @Size(max = 200, message = "El sitio web no puede exceder los 200 caracteres")
    private String sitioWeb;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;
    
    @Size(max = 100, message = "El sector no puede exceder los 100 caracteres")
    private String sector;
    
    private Integer numeroEmpleados;
    
    @Size(max = 100, message = "El contacto principal no puede exceder los 100 caracteres")
    private String contactoPrincipal;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El teléfono de contacto debe tener un formato válido")
    private String telefonoContacto;
    
    @Email(message = "El email de contacto debe tener un formato válido")
    private String emailContacto;
    
    private Boolean verificada;
    
    private Boolean activa;
}