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

import com.servicioagil.dto.CreateSubcategoriaDTO;
import com.servicioagil.dto.SubcategoriaConTecnicosDTO;
import com.servicioagil.dto.SubcategoriaDTO;
import com.servicioagil.dto.UpdateSubcategoriaDTO;
import com.servicioagil.service.SubcategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subcategorias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subcategorías", description = "API para gestionar subcategorías de servicios")
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    @GetMapping
    @Operation(summary = "Obtener todas las subcategorías con paginación y filtros",
               description = "Retorna una lista paginada de subcategorías con opción de filtrar por categoría, nombre y estado")
    @ApiResponse(responseCode = "200", description = "Lista de subcategorías obtenida exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerSubcategorias(
            @Parameter(description = "Filtrar por ID de categoría")
            @RequestParam(required = false) Long categoriaId,
            
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

        log.info("Obteniendo subcategorías - categoría: {}, página: {}, tamaño: {}, nombre: '{}', activo: {}", 
                 categoriaId, page, size, nombre, activo);
        
        Page<SubcategoriaDTO> subcategorias = subcategoriaService.obtenerSubcategorias(
                categoriaId, nombre, activo, page, size, sortBy, sortDir);
        
        Map<String, Object> response = new HashMap<>();
        response.put("subcategorias", subcategorias.getContent());
        response.put("paginaActual", subcategorias.getNumber());
        response.put("totalElementos", subcategorias.getTotalElements());
        response.put("totalPaginas", subcategorias.getTotalPages());
        response.put("tamanoPagina", subcategorias.getSize());
        response.put("esUltimaPagina", subcategorias.isLast());
        response.put("esPrimeraPagina", subcategorias.isFirst());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Obtener subcategorías activas de una categoría",
               description = "Retorna todas las subcategorías activas de una categoría específica sin paginación")
    @ApiResponse(responseCode = "200", description = "Lista de subcategorías activas obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    public ResponseEntity<Map<String, Object>> obtenerSubcategoriasActivasPorCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoriaId) {

        log.info("Obteniendo subcategorías activas de la categoría: {}", categoriaId);
        
        List<SubcategoriaDTO> subcategorias = subcategoriaService.obtenerSubcategoriasActivasPorCategoria(categoriaId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("subcategorias", subcategorias);
        response.put("categoriaId", categoriaId);
        response.put("total", subcategorias.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar subcategorías por término de búsqueda",
               description = "Busca subcategorías por nombre o descripción que contengan el término especificado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    public ResponseEntity<Map<String, Object>> buscarSubcategorias(
            @Parameter(description = "ID de categoría para filtrar la búsqueda")
            @RequestParam(required = false) Long categoriaId,
            
            @Parameter(description = "Término de búsqueda", required = true)
            @RequestParam String q,
            
            @Parameter(description = "Filtrar por estado activo")
            @RequestParam(required = false) Boolean activo,
            
            @Parameter(description = "Número de página (empezando en 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página")
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando subcategorías con término: '{}', categoría: {}, activo: {}", q, categoriaId, activo);
        
        Page<SubcategoriaDTO> subcategorias = subcategoriaService.buscarSubcategorias(categoriaId, q, activo, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("subcategorias", subcategorias.getContent());
        response.put("terminoBusqueda", q);
        response.put("categoriaId", categoriaId);
        response.put("paginaActual", subcategorias.getNumber());
        response.put("totalElementos", subcategorias.getTotalElements());
        response.put("totalPaginas", subcategorias.getTotalPages());
        response.put("tamanoPagina", subcategorias.getSize());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener subcategoría por ID",
               description = "Retorna los detalles de una subcategoría específica")
    @ApiResponse(responseCode = "200", description = "Subcategoría encontrada exitosamente")
    @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada", content = @Content)
    public ResponseEntity<Map<String, Object>> obtenerSubcategoriaPorId(
            @Parameter(description = "ID de la subcategoría", required = true)
            @PathVariable Long id) {

        log.info("Obteniendo subcategoría con ID: {}", id);
        
        SubcategoriaDTO subcategoria = subcategoriaService.obtenerSubcategoriaPorId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("subcategoria", subcategoria);
        
        return ResponseEntity.ok(response);
    }



    @PostMapping
    @Operation(summary = "Crear nueva subcategoría",
               description = "Crea una nueva subcategoría dentro de una categoría")
    @ApiResponse(responseCode = "201", description = "Subcategoría creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe una subcategoría con ese nombre en la categoría", content = @Content)
    public ResponseEntity<Map<String, Object>> crearSubcategoria(
            @Parameter(description = "Datos de la nueva subcategoría", required = true)
            @Valid @RequestBody CreateSubcategoriaDTO createDTO) {

        log.info("Creando nueva subcategoría: {} en categoría: {}", createDTO.getNombre(), createDTO.getCategoriaId());
        
        SubcategoriaDTO subcategoria = subcategoriaService.crearSubcategoria(createDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Subcategoría creada exitosamente");
        response.put("subcategoria", subcategoria);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar subcategoría existente",
               description = "Actualiza los datos de una subcategoría existente")
    @ApiResponse(responseCode = "200", description = "Subcategoría actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe otra subcategoría con ese nombre en la categoría", content = @Content)
    public ResponseEntity<Map<String, Object>> actualizarSubcategoria(
            @Parameter(description = "ID de la subcategoría", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Datos actualizados de la subcategoría", required = true)
            @Valid @RequestBody UpdateSubcategoriaDTO updateDTO) {

        log.info("Actualizando subcategoría con ID: {}", id);
        
        SubcategoriaDTO subcategoria = subcategoriaService.actualizarSubcategoria(id, updateDTO);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Subcategoría actualizada exitosamente");
        response.put("subcategoria", subcategoria);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar subcategoría (soft delete)",
               description = "Marca una subcategoría como inactiva")
    @ApiResponse(responseCode = "200", description = "Subcategoría eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada", content = @Content)
    public ResponseEntity<Map<String, Object>> eliminarSubcategoria(
            @Parameter(description = "ID de la subcategoría", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Usuario que elimina la subcategoría")
            @RequestParam(required = false) String eliminadoPor) {

        log.info("Eliminando subcategoría con ID: {}", id);
        
        subcategoriaService.eliminarSubcategoria(id, eliminadoPor);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Subcategoría eliminada exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categoria/{categoriaId}/reordenar")
    @Operation(summary = "Reordenar subcategorías dentro de una categoría",
               description = "Actualiza el orden de visualización de las subcategorías dentro de una categoría")
    @ApiResponse(responseCode = "200", description = "Subcategorías reordenadas exitosamente")
    @ApiResponse(responseCode = "400", description = "Lista de IDs inválida", content = @Content)
    @ApiResponse(responseCode = "404", description = "Categoría o una o más subcategorías no encontradas", content = @Content)
    public ResponseEntity<Map<String, Object>> reordenarSubcategorias(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoriaId,
            
            @Parameter(description = "Lista de IDs de subcategorías en el nuevo orden", required = true)
            @RequestBody List<Long> idsOrdenados) {

        log.info("Reordenando {} subcategorías en la categoría: {}", idsOrdenados.size(), categoriaId);
        
        subcategoriaService.reordenarSubcategorias(categoriaId, idsOrdenados);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Subcategorías reordenadas exitosamente");
        response.put("categoriaId", categoriaId);
        response.put("totalReordenadas", idsOrdenados.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filtros")
    @Operation(summary = "Obtener subcategorías con filtros específicos",
               description = "Busca subcategorías que cumplan con criterios específicos de filtro")
    @ApiResponse(responseCode = "200", description = "Subcategorías filtradas obtenidas exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerSubcategoriasConFiltros(
            @Parameter(description = "ID de categoría para filtrar")
            @RequestParam(required = false) Long categoriaId,
            
            @Parameter(description = "Permite servicio urgente")
            @RequestParam(required = false) Boolean urgente,
            
            @Parameter(description = "Incluye materiales")
            @RequestParam(required = false) Boolean conMateriales,
            
            @Parameter(description = "Emite boleta o factura")
            @RequestParam(required = false) Boolean boletaFactura,
            
            @Parameter(description = "Ofrece garantía")
            @RequestParam(required = false) Boolean garantia,
            
            @Parameter(description = "Trabajo en interior")
            @RequestParam(required = false) Boolean interior,
            
            @Parameter(description = "Trabajo en exterior")
            @RequestParam(required = false) Boolean exterior,
            
            @Parameter(description = "Trabajo en altura")
            @RequestParam(required = false) Boolean altura,
            
            @Parameter(description = "Servicio a domicilio")
            @RequestParam(required = false) Boolean domicilio,
            
            @Parameter(description = "Atención 24/7")
            @RequestParam(required = false) Boolean atencion24h,
            
            @Parameter(description = "Radio de cobertura máximo en km")
            @RequestParam(required = false) Integer radioMaximo,
            
            @Parameter(description = "Tipo de precio")
            @RequestParam(required = false) String tipoPrecio,
            
            @Parameter(description = "Rating mínimo requerido")
            @RequestParam(required = false) Double ratingMinimo,
            
            @Parameter(description = "Número de página (empezando en 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página")
            @RequestParam(defaultValue = "10") int size) {

        log.info("Obteniendo subcategorías con filtros específicos - categoría: {}, urgente: {}, conMateriales: {}, etc.", 
                 categoriaId, urgente, conMateriales);
        
        Page<SubcategoriaDTO> subcategorias = subcategoriaService.obtenerSubcategoriasConFiltros(
                categoriaId, urgente, conMateriales, boletaFactura, garantia, interior, exterior, 
                altura, domicilio, atencion24h, radioMaximo, tipoPrecio, ratingMinimo, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("subcategorias", subcategorias.getContent());
        Map<String, Object> filtrosAplicados = new HashMap<>();
        filtrosAplicados.put("categoriaId", categoriaId);
        filtrosAplicados.put("urgente", urgente);
        filtrosAplicados.put("conMateriales", conMateriales);
        filtrosAplicados.put("boletaFactura", boletaFactura);
        filtrosAplicados.put("garantia", garantia);
        filtrosAplicados.put("interior", interior);
        filtrosAplicados.put("exterior", exterior);
        filtrosAplicados.put("altura", altura);
        filtrosAplicados.put("domicilio", domicilio);
        filtrosAplicados.put("atencion24h", atencion24h);
        filtrosAplicados.put("radioMaximo", radioMaximo);
        filtrosAplicados.put("tipoPrecio", tipoPrecio);
        filtrosAplicados.put("ratingMinimo", ratingMinimo);
        
        response.put("filtrosAplicados", filtrosAplicados);
        response.put("paginaActual", subcategorias.getNumber());
        response.put("totalElementos", subcategorias.getTotalElements());
        response.put("totalPaginas", subcategorias.getTotalPages());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/tecnicos")
    @Operation(summary = "Obtener subcategoría con sus técnicos",
               description = "Retorna una subcategoría con la lista completa de técnicos activos y disponibles asociados a ella")
    @ApiResponse(responseCode = "200", description = "Subcategoría con técnicos obtenida exitosamente",
                 content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Subcategoría no encontrada")
    public ResponseEntity<SubcategoriaConTecnicosDTO> obtenerSubcategoriaConTecnicos(
            @Parameter(description = "ID de la subcategoría", required = true)
            @PathVariable Long id) {
        log.info("GET /api/subcategorias/{}/tecnicos - Obteniendo subcategoría con técnicos", id);
        SubcategoriaConTecnicosDTO subcategoria = subcategoriaService.obtenerSubcategoriaConTecnicos(id);
        return ResponseEntity.ok(subcategoria);
    }
}