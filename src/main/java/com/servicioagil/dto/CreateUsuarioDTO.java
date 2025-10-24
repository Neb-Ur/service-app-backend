package com.servicioagil.dto;

import java.time.LocalDateTime;

import com.servicioagil.entity.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUsuarioDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El teléfono debe tener un formato válido")
    private String telefono;
    
    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    private String direccion;
    
    private LocalDateTime fechaNacimiento;
    
    @NotNull(message = "El tipo de usuario es obligatorio")
    private Usuario.TipoUsuario tipoUsuario;
    
    private Boolean activo = true;
}