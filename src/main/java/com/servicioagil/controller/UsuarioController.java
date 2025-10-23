package com.servicioagil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicioagil.dto.CreateUsuarioDTO;
import com.servicioagil.dto.UpdateUsuarioDTO;
import com.servicioagil.dto.UsuarioDTO;
import com.servicioagil.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerTodosLosUsuarios() {
        log.info("GET /api/usuarios - Obteniendo todos los usuarios");
        
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuarios);
        response.put("count", usuarios.size());
        response.put("message", "Usuarios obtenidos exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        log.info("GET /api/usuarios/{} - Obteniendo usuario por ID", id);
        
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuario);
        response.put("message", "Usuario encontrado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener usuarios activos", description = "Retorna una lista de usuarios activos")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios activos obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerUsuariosActivos() {
        log.info("GET /api/usuarios/activos - Obteniendo usuarios activos");
        
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosActivos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuarios);
        response.put("count", usuarios.size());
        response.put("message", "Usuarios activos obtenidos exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuarios por nombre", description = "Busca usuarios que contengan el texto especificado en su nombre")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<Map<String, Object>> buscarUsuariosPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", required = true)
            @RequestParam String nombre) {
        log.info("GET /api/usuarios/buscar?nombre={} - Buscando usuarios por nombre", nombre);
        
        List<UsuarioDTO> usuarios = usuarioService.buscarUsuariosPorNombre(nombre);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuarios);
        response.put("count", usuarios.size());
        response.put("message", "Búsqueda completada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe")
    })
    public ResponseEntity<Map<String, Object>> crearUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @Valid @RequestBody CreateUsuarioDTO createUsuarioDTO) {
        log.info("POST /api/usuarios - Creando nuevo usuario");
        
        UsuarioDTO usuario = usuarioService.crearUsuario(createUsuarioDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuario);
        response.put("message", "Usuario creado exitosamente");
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya existe")
    })
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos a actualizar", required = true)
            @Valid @RequestBody UpdateUsuarioDTO updateUsuarioDTO) {
        log.info("PUT /api/usuarios/{} - Actualizando usuario", id);
        
        UsuarioDTO usuario = usuarioService.actualizarUsuario(id, updateUsuarioDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", usuario);
        response.put("message", "Usuario actualizado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Map<String, Object>> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable Long id) {
        log.info("DELETE /api/usuarios/{} - Eliminando usuario", id);
        
        usuarioService.eliminarUsuario(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Usuario eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de usuarios", description = "Retorna estadísticas generales de usuarios")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        log.info("GET /api/usuarios/estadisticas - Obteniendo estadísticas");
        
        long usuariosActivos = usuarioService.contarUsuariosActivos();
        long usuariosInactivos = usuarioService.contarUsuariosInactivos();
        long totalUsuarios = usuariosActivos + usuariosInactivos;
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("total", totalUsuarios);
        estadisticas.put("activos", usuariosActivos);
        estadisticas.put("inactivos", usuariosInactivos);
        estadisticas.put("porcentajeActivos", totalUsuarios > 0 ? (usuariosActivos * 100.0 / totalUsuarios) : 0);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", estadisticas);
        response.put("message", "Estadísticas obtenidas exitosamente");
        
        return ResponseEntity.ok(response);
    }
}