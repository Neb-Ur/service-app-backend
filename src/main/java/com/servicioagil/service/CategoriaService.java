package com.servicioagil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.servicioagil.dto.CategoriaDTO;
import com.servicioagil.dto.CreateCategoriaDTO;
import com.servicioagil.dto.SubcategoriaDTO;
import com.servicioagil.dto.UpdateCategoriaDTO;
import com.servicioagil.entity.Categoria;
import com.servicioagil.exception.BusinessException;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    /**
     * Obtener todas las categorías con paginación y filtros
     */
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> obtenerCategorias(String nombre, Boolean activo, int page, int size, String sortBy, String sortDir) {
        log.debug("Obteniendo categorías - página: {}, tamaño: {}, filtro nombre: {}, activo: {}", page, size, nombre, activo);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Categoria> categorias;
        
        if (StringUtils.hasText(nombre) && activo != null) {
            categorias = categoriaRepository.findBySearchTerm(nombre, activo, pageable);
        } else if (StringUtils.hasText(nombre)) {
            categorias = categoriaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre, pageable);
        } else if (activo != null) {
            categorias = categoriaRepository.findByActivo(activo, pageable);
        } else {
            categorias = categoriaRepository.findAll(pageable);
        }
        
        return categorias.map(this::convertirACategoriaDTO);
    }

    /**
     * Obtener todas las categorías activas sin paginación (para selects)
     */
    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerCategoriasActivas() {
        log.debug("Obteniendo todas las categorías activas");
        List<Categoria> categorias = categoriaRepository.findByActivoTrueOrderByOrdenVisualizacionAscNombreAsc();
        return categorias.stream()
                .map(this::convertirACategoriaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar categorías por término de búsqueda
     */
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> buscarCategorias(String searchTerm, Boolean activo, int page, int size) {
        log.debug("Buscando categorías con término: '{}', activo: {}", searchTerm, activo);
        
        Pageable pageable = PageRequest.of(page, size, 
                Sort.by(Sort.Direction.ASC, "ordenVisualizacion", "nombre"));
        
        Page<Categoria> categorias = categoriaRepository.findBySearchTerm(searchTerm, activo != null ? activo : true, pageable);
        return categorias.map(this::convertirACategoriaDTO);
    }

    /**
     * Obtener categoría por ID
     */
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        log.debug("Obteniendo categoría con ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        return convertirACategoriaDTO(categoria);
    }

    /**
     * Obtener categoría con subcategorías
     */
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerCategoriaConSubcategorias(Long id) {
        log.debug("Obteniendo categoría con subcategorías, ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        CategoriaDTO categoriaDTO = convertirACategoriaDTO(categoria);
        
        // Convertir subcategorías activas
        List<SubcategoriaDTO> subcategoriasDTO = categoria.getSubcategorias().stream()
                .filter(s -> s.getActivo())
                .map(s -> {
                    SubcategoriaDTO dto = modelMapper.map(s, SubcategoriaDTO.class);
                    dto.setCategoriaId(categoria.getId());
                    dto.setCategoriaNombre(categoria.getNombre());
                    return dto;
                })
                .collect(Collectors.toList());
        
        categoriaDTO.setSubcategorias(subcategoriasDTO);
        
        return categoriaDTO;
    }

    /**
     * Crear nueva categoría
     */
    public CategoriaDTO crearCategoria(CreateCategoriaDTO createDTO) {
        log.debug("Creando nueva categoría: {}", createDTO.getNombre());
        
        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByNombreIgnoreCase(createDTO.getNombre())) {
            throw new BusinessException("Ya existe una categoría con el nombre: " + createDTO.getNombre());
        }
        
        Categoria categoria = modelMapper.map(createDTO, Categoria.class);
        
        // Asignar orden de visualización si no se especificó
        if (categoria.getOrdenVisualizacion() == null) {
            Integer maxOrden = categoriaRepository.findMaxOrdenVisualizacion();
            categoria.setOrdenVisualizacion(maxOrden + 1);
        }
        
        categoria = categoriaRepository.save(categoria);
        log.info("Categoría creada exitosamente con ID: {}", categoria.getId());
        
        return convertirACategoriaDTO(categoria);
    }

    /**
     * Actualizar categoría existente
     */
    public CategoriaDTO actualizarCategoria(Long id, UpdateCategoriaDTO updateDTO) {
        log.debug("Actualizando categoría con ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Validar que no exista otra categoría con el mismo nombre
        if (StringUtils.hasText(updateDTO.getNombre()) && 
            categoriaRepository.existsByNombreIgnoreCaseAndIdNot(updateDTO.getNombre(), id)) {
            throw new BusinessException("Ya existe otra categoría con el nombre: " + updateDTO.getNombre());
        }
        
        // Actualizar campos no nulos
        if (StringUtils.hasText(updateDTO.getNombre())) {
            categoria.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getDescripcion() != null) {
            categoria.setDescripcion(updateDTO.getDescripcion());
        }
        if (updateDTO.getActivo() != null) {
            categoria.setActivo(updateDTO.getActivo());
        }
        if (updateDTO.getOrdenVisualizacion() != null) {
            categoria.setOrdenVisualizacion(updateDTO.getOrdenVisualizacion());
        }
        if (updateDTO.getColorHexadecimal() != null) {
            categoria.setColorHexadecimal(updateDTO.getColorHexadecimal());
        }
        if (updateDTO.getIcono() != null) {
            categoria.setIcono(updateDTO.getIcono());
        }
        if (StringUtils.hasText(updateDTO.getModificadoPor())) {
            categoria.setModificadoPor(updateDTO.getModificadoPor());
        }
        
        categoria = categoriaRepository.save(categoria);
        log.info("Categoría actualizada exitosamente con ID: {}", categoria.getId());
        
        return convertirACategoriaDTO(categoria);
    }

    /**
     * Eliminar categoría (soft delete)
     */
    public void eliminarCategoria(Long id, String eliminadoPor) {
        log.debug("Eliminando categoría con ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar que no tenga subcategorías activas
        Long subcategoriasActivas = categoriaRepository.countActiveSubcategoriasByCategoriaId(id);
        if (subcategoriasActivas > 0) {
            throw new BusinessException("No se puede eliminar la categoría porque tiene " + subcategoriasActivas + " subcategorías activas");
        }
        
        categoria.setActivo(false);
        if (StringUtils.hasText(eliminadoPor)) {
            categoria.setModificadoPor(eliminadoPor);
        }
        
        categoriaRepository.save(categoria);
        log.info("Categoría eliminada (soft delete) exitosamente con ID: {}", categoria.getId());
    }

    /**
     * Reordenar categorías
     */
    public void reordenarCategorias(List<Long> idsOrdenados) {
        log.debug("Reordenando {} categorías", idsOrdenados.size());
        
        for (int i = 0; i < idsOrdenados.size(); i++) {
            Long id = idsOrdenados.get(i);
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
            
            categoria.setOrdenVisualizacion(i + 1);
            categoriaRepository.save(categoria);
        }
        
        log.info("Reordenamiento de categorías completado exitosamente");
    }

    /**
     * Convertir Categoria a CategoriaDTO
     */
    private CategoriaDTO convertirACategoriaDTO(Categoria categoria) {
        CategoriaDTO dto = modelMapper.map(categoria, CategoriaDTO.class);
        
        // Contar subcategorías activas
        Long totalSubcategorias = categoriaRepository.countActiveSubcategoriasByCategoriaId(categoria.getId());
        dto.setTotalSubcategorias(totalSubcategorias);
        
        return dto;
    }


}