package com.servicioagil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
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

import com.servicioagil.dto.CategoriaDTO;
import com.servicioagil.dto.CreateCategoriaDTO;
import com.servicioagil.dto.UpdateCategoriaDTO;
import com.servicioagil.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Categorías", description = "API para gestionar categorías de servicios")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías con paginación y filtros",
               description = "Retorna una lista paginada de categorías con opción de filtrar por nombre y estado")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerCategorias(
            @Parameter(description = "Filtrar por nombre (búsqueda parcial)")
            @RequestParam(required = false) String nombre,
            
            @Parameter(description = "Filtrar por estado activo")
            @RequestParam(required = false) Boolean activo,
            
            @Parameter(description = "Número de página (empezando en 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo por el cual ordenar")
            @RequestParam(defaultValue = "ordenVisualizacion") String sortBy,
            
            @Parameter(description = "Dirección del ordenamiento (ASC o DESC)")
            @RequestParam(defaultValue = "ASC") String sortDir) {

        log.info("Obteniendo categorías - página: {}, tamaño: {}, nombre: '{}', activo: {}", page, size, nombre, activo);
        
        Page<CategoriaDTO> categorias = categoriaService.obtenerCategorias(nombre, activo, page, size, sortBy, sortDir);
        
        Map<String, Object> response = new HashMap<>();
        response.put("categorias", categorias.getContent());
        response.put("paginaActual", categorias.getNumber());
        response.put("totalElementos", categorias.getTotalElements());
        response.put("totalPaginas", categorias.getTotalPages());
        response.put("tamanoPagina", categorias.getSize());
        response.put("esUltimaPagina", categorias.isLast());
        response.put("esPrimeraPagina", categorias.isFirst());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener todas las categorías activas sin paginación",
               description = "Retorna una lista de todas las categorías activas, útil para selects y dropdowns")
    @ApiResponse(responseCode = "200", description = "Lista de categorías activas obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerCategoriasActivas() {
        log.info("Obteniendo todas las categorías activas");
        
        List<CategoriaDTO> categorias = categoriaService.obtenerCategoriasActivas();
        
        Map<String, Object> response = new HashMap<>();
        response.put("categorias", categorias);
        response.put("total", categorias.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías por término de búsqueda",
               description = "Busca categorías por nombre o descripción que contengan el término especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<Map<String, Object>> buscarCategorias(
            @Parameter(description = "Término de búsqueda", required = true)
            @RequestParam String q,
            
            @Parameter(description = "Filtrar por estado activo")
            @RequestParam(required = false) Boolean activo,
            
            @Parameter(description = "Número de página (empezando en 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página")
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando categorías con término: '{}', activo: {}", q, activo);
        
        Page<CategoriaDTO> categorias = categoriaService.buscarCategorias(q, activo, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("categorias", categorias.getContent());
        response.put("terminoBusqueda", q);
        response.put("paginaActual", categorias.getNumber());
        response.put("totalElementos", categorias.getTotalElements());
        response.put("totalPaginas", categorias.getTotalPages());
        response.put("tamanoPagina", categorias.getSize());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID",
               description = "Retorna los detalles de una categoría específica")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<Map<String, Object>> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {

        log.info("Obteniendo categoría con ID: {}", id);
        
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("categoria", categoria);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/con-subcategorias")
    @Operation(summary = "Obtener categoría con sus subcategorías",
               description = "Retorna una categoría con todas sus subcategorías activas")
    @ApiResponse(responseCode = "200", description = "Categoría con subcategorías obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<Map<String, Object>> obtenerCategoriaConSubcategorias(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {

        log.info("Obteniendo categoría con subcategorías, ID: {}", id);
        
        CategoriaDTO categoria = categoriaService.obtenerCategoriaConSubcategorias(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("categoria", categoria);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría",
               description = "Crea una nueva categoría de servicios")
    @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre", content = @Content)
    public ResponseEntity<Map<String, Object>> crearCategoria(
            @Parameter(description = "Datos de la nueva categoría", required = true)
            @Valid @RequestBody CreateCategoriaDTO createDTO) {

        log.info("Creando nueva categoría: {}", createDTO.getNombre());
        
        CategoriaDTO categoria = categoriaService.crearCategoria(createDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Categoría creada exitosamente");
        response.put("categoria", categoria);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría existente",
               description = "Actualiza los datos de una categoría existente")
    @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe otra categoría con ese nombre", content = @Content)
    public ResponseEntity<Map<String, Object>> actualizarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Datos actualizados de la categoría", required = true)
            @Valid @RequestBody UpdateCategoriaDTO updateDTO) {

        log.info("Actualizando categoría con ID: {}", id);
        
        CategoriaDTO categoria = categoriaService.actualizarCategoria(id, updateDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Categoría actualizada exitosamente");
        response.put("categoria", categoria);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría (soft delete)",
               description = "Marca una categoría como inactiva. No se puede eliminar si tiene subcategorías activas")
    @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    @ApiResponse(responseCode = "409", description = "No se puede eliminar porque tiene subcategorías activas", content = @Content)
    public ResponseEntity<Map<String, Object>> eliminarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Usuario que elimina la categoría")
            @RequestParam(required = false) String eliminadoPor) {

        log.info("Eliminando categoría con ID: {}", id);
        
        categoriaService.eliminarCategoria(id, eliminadoPor);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Categoría eliminada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reordenar")
    @Operation(summary = "Reordenar categorías",
               description = "Actualiza el orden de visualización de las categorías")
    @ApiResponse(responseCode = "200", description = "Categorías reordenadas exitosamente")
    @ApiResponse(responseCode = "400", description = "Lista de IDs inválida", content = @Content)
    @ApiResponse(responseCode = "404", description = "Una o más categorías no encontradas", content = @Content)
    public ResponseEntity<Map<String, Object>> reordenarCategorias(
            @Parameter(description = "Lista de IDs en el nuevo orden", required = true)
            @RequestBody List<Long> idsOrdenados) {

        log.info("Reordenando {} categorías", idsOrdenados.size());
        
        categoriaService.reordenarCategorias(idsOrdenados);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Categorías reordenadas exitosamente");
        response.put("totalReordenadas", idsOrdenados.size());
        
        return ResponseEntity.ok(response);
    }
}