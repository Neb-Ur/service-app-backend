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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    @Column(name = "apellido", length = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El teléfono debe tener un formato válido")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    @Column(name = "direccion", length = 500)
    private String direccion;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private Double latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private Double longitude;

    @Column(name = "fecha_nacimiento")
    private LocalDateTime fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = true, length = 20)
    private TipoUsuario tipoUsuario = TipoUsuario.CLIENTE;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // Relación con empresa (solo para usuarios tipo EMPRESA)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Empresa empresa;

    // Relación con técnico (solo para usuarios tipo TECNICO)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Tecnico tecnico;

    // Relación con solicitudes realizadas por el usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Solicitud> solicitudes = new ArrayList<>();

    // Relación con reseñas escritas por el usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resena> resenasEscritas = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = true)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
        if (tipoUsuario == null) {
            tipoUsuario = TipoUsuario.CLIENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum TipoUsuario {
        CLIENTE,    // Usuario normal que solicita servicios
        TECNICO,    // Técnico que ofrece servicios
        EMPRESA     // Empresa que gestiona múltiples técnicos
    }
}