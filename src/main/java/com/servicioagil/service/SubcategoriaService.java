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

import com.servicioagil.dto.CreateSubcategoriaDTO;
import com.servicioagil.dto.SubcategoriaDTO;
import com.servicioagil.dto.UpdateSubcategoriaDTO;
import com.servicioagil.entity.Categoria;
import com.servicioagil.entity.Subcategoria;
import com.servicioagil.exception.BusinessException;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.CategoriaRepository;
import com.servicioagil.repository.SubcategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    /**
     * Obtener todas las subcategorías con paginación y filtros
     */
    @Transactional(readOnly = true)
    public Page<SubcategoriaDTO> obtenerSubcategorias(Long categoriaId, String nombre, Boolean activo, int page, int size, String sortBy, String sortDir) {
        log.debug("Obteniendo subcategorías - categoría: {}, página: {}, tamaño: {}, filtro nombre: {}, activo: {}", 
                  categoriaId, page, size, nombre, activo);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Subcategoria> subcategorias;
        
        if (categoriaId != null && StringUtils.hasText(nombre) && activo != null) {
            subcategorias = subcategoriaRepository.findBySearchTermAndCategoriaId(categoriaId, nombre, activo, pageable);
        } else if (categoriaId != null && StringUtils.hasText(nombre)) {
            subcategorias = subcategoriaRepository.findByCategoriaIdAndNombreContainingIgnoreCaseAndActivoTrue(categoriaId, nombre, pageable);
        } else if (categoriaId != null && activo != null) {
            subcategorias = subcategoriaRepository.findByCategoriaIdAndActivo(categoriaId, activo, pageable);
        } else if (categoriaId != null) {
            subcategorias = subcategoriaRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageable);
        } else if (StringUtils.hasText(nombre) && activo != null) {
            subcategorias = subcategoriaRepository.findBySearchTerm(nombre, activo, pageable);
        } else if (StringUtils.hasText(nombre)) {
            subcategorias = subcategoriaRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre, pageable);
        } else if (activo != null) {
            subcategorias = subcategoriaRepository.findByActivo(activo, pageable);
        } else {
            subcategorias = subcategoriaRepository.findAll(pageable);
        }
        
        return subcategorias.map(this::convertirASubcategoriaDTO);
    }

    /**
     * Obtener todas las subcategorías activas de una categoría sin paginación
     */
    @Transactional(readOnly = true)
    public List<SubcategoriaDTO> obtenerSubcategoriasActivasPorCategoria(Long categoriaId) {
        log.debug("Obteniendo subcategorías activas de la categoría: {}", categoriaId);
        
        // Verificar que la categoría exista
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId);
        }
        
        List<Subcategoria> subcategorias = subcategoriaRepository.findByCategoriaIdAndActivoTrueOrderByOrdenVisualizacionAscNombreAsc(categoriaId);
        return subcategorias.stream()
                .map(this::convertirASubcategoriaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar subcategorías por término de búsqueda
     */
    @Transactional(readOnly = true)
    public Page<SubcategoriaDTO> buscarSubcategorias(Long categoriaId, String searchTerm, Boolean activo, int page, int size) {
        log.debug("Buscando subcategorías con término: '{}', categoría: {}, activo: {}", searchTerm, categoriaId, activo);
        
        Pageable pageable = PageRequest.of(page, size, 
                Sort.by(Sort.Direction.ASC, "ordenVisualizacion", "nombre"));
        
        Page<Subcategoria> subcategorias;
        
        if (categoriaId != null) {
            subcategorias = subcategoriaRepository.findBySearchTermAndCategoriaId(categoriaId, searchTerm, activo != null ? activo : true, pageable);
        } else {
            subcategorias = subcategoriaRepository.findBySearchTerm(searchTerm, activo != null ? activo : true, pageable);
        }
        
        return subcategorias.map(this::convertirASubcategoriaDTO);
    }

    /**
     * Obtener subcategoría por ID
     */
    @Transactional(readOnly = true)
    public SubcategoriaDTO obtenerSubcategoriaPorId(Long id) {
        log.debug("Obteniendo subcategoría con ID: {}", id);
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + id));
        
        return convertirASubcategoriaDTO(subcategoria);
    }



    /**
     * Crear nueva subcategoría
     */
    public SubcategoriaDTO crearSubcategoria(CreateSubcategoriaDTO createDTO) {
        log.debug("Creando nueva subcategoría: {} en categoría: {}", createDTO.getNombre(), createDTO.getCategoriaId());
        
        // Verificar que la categoría exista y esté activa
        Categoria categoria = categoriaRepository.findById(createDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + createDTO.getCategoriaId()));
        
        if (!categoria.getActivo()) {
            throw new BusinessException("No se puede crear una subcategoría en una categoría inactiva");
        }
        
        // Validar que no exista una subcategoría con el mismo nombre en la misma categoría
        if (subcategoriaRepository.existsByNombreIgnoreCaseAndCategoriaId(createDTO.getNombre(), createDTO.getCategoriaId())) {
            throw new BusinessException("Ya existe una subcategoría con el nombre '" + createDTO.getNombre() + "' en esta categoría");
        }
        
        Subcategoria subcategoria = modelMapper.map(createDTO, Subcategoria.class);
        subcategoria.setCategoria(categoria);
        
        // Asignar orden de visualización si no se especificó
        if (subcategoria.getOrdenVisualizacion() == null) {
            Integer maxOrden = subcategoriaRepository.findMaxOrdenVisualizacionByCategoriaId(createDTO.getCategoriaId());
            subcategoria.setOrdenVisualizacion(maxOrden + 1);
        }
        
        subcategoria = subcategoriaRepository.save(subcategoria);
        log.info("Subcategoría creada exitosamente con ID: {}", subcategoria.getId());
        
        return convertirASubcategoriaDTO(subcategoria);
    }

    /**
     * Actualizar subcategoría existente
     */
    public SubcategoriaDTO actualizarSubcategoria(Long id, UpdateSubcategoriaDTO updateDTO) {
        log.debug("Actualizando subcategoría con ID: {}", id);
        
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + id));
        
        // Si se va a cambiar la categoría, verificar que la nueva categoría exista y esté activa
        if (updateDTO.getCategoriaId() != null && !updateDTO.getCategoriaId().equals(subcategoria.getCategoria().getId())) {
            Categoria nuevaCategoria = categoriaRepository.findById(updateDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + updateDTO.getCategoriaId()));
            
            if (!nuevaCategoria.getActivo()) {
                throw new BusinessException("No se puede mover la subcategoría a una categoría inactiva");
            }
            
            subcategoria.setCategoria(nuevaCategoria);
        }
        
        // Validar que no exista otra subcategoría con el mismo nombre en la misma categoría
        if (StringUtils.hasText(updateDTO.getNombre())) {
            Long categoriaId = updateDTO.getCategoriaId() != null ? updateDTO.getCategoriaId() : subcategoria.getCategoria().getId();
            if (subcategoriaRepository.existsByNombreIgnoreCaseAndCategoriaIdAndIdNot(updateDTO.getNombre(), categoriaId, id)) {
                throw new BusinessException("Ya existe otra subcategoría con el nombre '" + updateDTO.getNombre() + "' en esta categoría");
            }
        }
        
        // Actualizar campos no nulos
        if (StringUtils.hasText(updateDTO.getNombre())) {
            subcategoria.setNombre(updateDTO.getNombre());
        }
        if (updateDTO.getDescripcion() != null) {
            subcategoria.setDescripcion(updateDTO.getDescripcion());
        }
        if (updateDTO.getActivo() != null) {
            subcategoria.setActivo(updateDTO.getActivo());
        }
        if (updateDTO.getOrdenVisualizacion() != null) {
            subcategoria.setOrdenVisualizacion(updateDTO.getOrdenVisualizacion());
        }
        if (updateDTO.getColorHexadecimal() != null) {
            subcategoria.setColorHexadecimal(updateDTO.getColorHexadecimal());
        }
        if (updateDTO.getIcono() != null) {
            subcategoria.setIcono(updateDTO.getIcono());
        }
        if (StringUtils.hasText(updateDTO.getModificadoPor())) {
            subcategoria.setModificadoPor(updateDTO.getModificadoPor());
        }
        
        subcategoria = subcategoriaRepository.save(subcategoria);
        log.info("Subcategoría actualizada exitosamente con ID: {}", subcategoria.getId());
        
        return convertirASubcategoriaDTO(subcategoria);
    }

    /**
     * Eliminar subcategoría (soft delete)
     */
    public void eliminarSubcategoria(Long id, String eliminadoPor) {
        log.debug("Eliminando subcategoría con ID: {}", id);
        
        Subcategoria subcategoria = subcategoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + id));
        

        
        subcategoria.setActivo(false);
        if (StringUtils.hasText(eliminadoPor)) {
            subcategoria.setModificadoPor(eliminadoPor);
        }
        
        subcategoriaRepository.save(subcategoria);
        log.info("Subcategoría eliminada (soft delete) exitosamente con ID: {}", subcategoria.getId());
    }

    /**
     * Reordenar subcategorías dentro de una categoría
     */
    public void reordenarSubcategorias(Long categoriaId, List<Long> idsOrdenados) {
        log.debug("Reordenando {} subcategorías en la categoría: {}", idsOrdenados.size(), categoriaId);
        
        // Verificar que la categoría exista
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId);
        }
        
        for (int i = 0; i < idsOrdenados.size(); i++) {
            Long id = idsOrdenados.get(i);
            Subcategoria subcategoria = subcategoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada con ID: " + id));
            
            // Verificar que la subcategoría pertenezca a la categoría especificada
            if (!subcategoria.getCategoria().getId().equals(categoriaId)) {
                throw new BusinessException("La subcategoría con ID " + id + " no pertenece a la categoría especificada");
            }
            
            subcategoria.setOrdenVisualizacion(i + 1);
            subcategoriaRepository.save(subcategoria);
        }
        
        log.info("Reordenamiento de subcategorías completado exitosamente para la categoría: {}", categoriaId);
    }

    /**
     * Obtener subcategorías con filtros específicos
     */
    @Transactional(readOnly = true)
    public Page<SubcategoriaDTO> obtenerSubcategoriasConFiltros(Long categoriaId, Boolean urgente, Boolean conMateriales, 
                                                               Boolean boletaFactura, Boolean garantia, Boolean interior, 
                                                               Boolean exterior, Boolean altura, Boolean domicilio, 
                                                               Boolean atencion24h, Integer radioMaximo, String tipoPrecio, 
                                                               Double ratingMinimo, int page, int size) {
        log.debug("Obteniendo subcategorías con filtros específicos");
        
        Pageable pageable = PageRequest.of(page, size, 
                Sort.by(Sort.Direction.ASC, "ordenVisualizacion", "nombre"));
        
        Page<Subcategoria> subcategorias = subcategoriaRepository.findSubcategoriasConFiltros(
                categoriaId, urgente, conMateriales, boletaFactura, garantia, interior, exterior, 
                altura, domicilio, atencion24h, radioMaximo, tipoPrecio, ratingMinimo, pageable);
        
        return subcategorias.map(this::convertirASubcategoriaDTO);
    }

    /**
     * Convertir Subcategoria a SubcategoriaDTO
     */
    private SubcategoriaDTO convertirASubcategoriaDTO(Subcategoria subcategoria) {
        SubcategoriaDTO dto = modelMapper.map(subcategoria, SubcategoriaDTO.class);
        
        // Asignar información de la categoría padre
        dto.setCategoriaId(subcategoria.getCategoria().getId());
        dto.setCategoriaNombre(subcategoria.getCategoria().getNombre());
        
        return dto;
    }
}