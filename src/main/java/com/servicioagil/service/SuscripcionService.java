package com.servicioagil.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicioagil.dto.CreateSuscripcionDTO;
import com.servicioagil.dto.SuscripcionDTO;
import com.servicioagil.dto.UpdateSuscripcionDTO;
import com.servicioagil.entity.Suscripcion;
import com.servicioagil.exception.BusinessException;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.SuscripcionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getAllSuscripciones() {
        log.info("Obteniendo todas las suscripciones");
        return suscripcionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getSuscripcionesActivas() {
        log.info("Obteniendo suscripciones activas");
        return suscripcionRepository.findByActivoTrueOrderByOrdenVisualizacionAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getSuscripcionesDestacadas() {
        log.info("Obteniendo suscripciones destacadas");
        return suscripcionRepository.findByDestacadoTrueAndActivoTrueOrderByOrdenVisualizacionAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SuscripcionDTO getSuscripcionById(Long id) {
        log.info("Obteniendo suscripción con id: {}", id);
        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada con id: " + id));
        return convertToDTO(suscripcion);
    }

    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getSuscripcionesPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio) {
        log.info("Obteniendo suscripciones con precio entre {} y {}", minPrecio, maxPrecio);
        if (minPrecio.compareTo(BigDecimal.ZERO) < 0 || maxPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Los precios deben ser mayores o iguales a 0");
        }
        if (minPrecio.compareTo(maxPrecio) > 0) {
            throw new BusinessException("El precio mínimo no puede ser mayor que el precio máximo");
        }
        return suscripcionRepository.findByPrecioFinalBetween(minPrecio, maxPrecio).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getSuscripcionesConDescuento() {
        log.info("Obteniendo suscripciones con descuento");
        return suscripcionRepository.findSuscripcionesConDescuento().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SuscripcionDTO createSuscripcion(CreateSuscripcionDTO createDTO) {
        log.info("Creando nueva suscripción: {}", createDTO.getNombre());

        // Validar que no exista una suscripción con el mismo nombre
        if (suscripcionRepository.existsByNombreIgnoreCase(createDTO.getNombre())) {
            throw new BusinessException("Ya existe una suscripción con el nombre: " + createDTO.getNombre());
        }

        Suscripcion suscripcion = new Suscripcion();
        mapCreateDTOToEntity(createDTO, suscripcion);

        Suscripcion savedSuscripcion = suscripcionRepository.save(suscripcion);
        log.info("Suscripción creada exitosamente con id: {}", savedSuscripcion.getId());
        return convertToDTO(savedSuscripcion);
    }

    @Transactional
    public SuscripcionDTO updateSuscripcion(Long id, UpdateSuscripcionDTO updateDTO) {
        log.info("Actualizando suscripción con id: {}", id);

        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada con id: " + id));

        // Validar nombre único si se está actualizando
        if (updateDTO.getNombre() != null && !updateDTO.getNombre().isBlank()) {
            if (suscripcionRepository.existsByNombreIgnoreCaseAndIdNot(updateDTO.getNombre(), id)) {
                throw new BusinessException("Ya existe otra suscripción con el nombre: " + updateDTO.getNombre());
            }
        }

        mapUpdateDTOToEntity(updateDTO, suscripcion);

        Suscripcion updatedSuscripcion = suscripcionRepository.save(suscripcion);
        log.info("Suscripción actualizada exitosamente con id: {}", id);
        return convertToDTO(updatedSuscripcion);
    }

    @Transactional
    public void deleteSuscripcion(Long id) {
        log.info("Eliminando suscripción con id: {}", id);

        if (!suscripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Suscripción no encontrada con id: " + id);
        }

        suscripcionRepository.deleteById(id);
        log.info("Suscripción eliminada exitosamente con id: {}", id);
    }

    @Transactional
    public SuscripcionDTO toggleActivoSuscripcion(Long id) {
        log.info("Cambiando estado activo de suscripción con id: {}", id);

        Suscripcion suscripcion = suscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suscripción no encontrada con id: " + id));

        suscripcion.setActivo(!suscripcion.getActivo());
        Suscripcion updatedSuscripcion = suscripcionRepository.save(suscripcion);

        log.info("Estado activo de suscripción cambiado a: {}", updatedSuscripcion.getActivo());
        return convertToDTO(updatedSuscripcion);
    }

    @Transactional(readOnly = true)
    public long contarSuscripcionesActivas() {
        return suscripcionRepository.countByActivoTrue();
    }

    private SuscripcionDTO convertToDTO(Suscripcion suscripcion) {
        SuscripcionDTO dto = new SuscripcionDTO();
        dto.setId(suscripcion.getId());
        dto.setNombre(suscripcion.getNombre());
        dto.setDescripcion(suscripcion.getDescripcion());
        dto.setPrecioOriginal(suscripcion.getPrecioOriginal());
        dto.setPorcentajeDescuento(suscripcion.getPorcentajeDescuento());
        dto.setPrecioFinal(suscripcion.getPrecioFinal());
        dto.setDuracionDias(suscripcion.getDuracionDias());
        dto.setNumeroSolicitudesIncluidas(suscripcion.getNumeroSolicitudesIncluidas());
        dto.setSoportePrioritario(suscripcion.getSoportePrioritario());
        dto.setDestacado(suscripcion.getDestacado());
        dto.setActivo(suscripcion.getActivo());
        dto.setOrdenVisualizacion(suscripcion.getOrdenVisualizacion());
        dto.setColorHexadecimal(suscripcion.getColorHexadecimal());
        dto.setIcono(suscripcion.getIcono());
        dto.setFechaCreacion(suscripcion.getFechaCreacion());
        dto.setFechaModificacion(suscripcion.getFechaModificacion());
        dto.setCreadoPor(suscripcion.getCreadoPor());
        dto.setModificadoPor(suscripcion.getModificadoPor());
        return dto;
    }

    private void mapCreateDTOToEntity(CreateSuscripcionDTO dto, Suscripcion entity) {
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecioOriginal(dto.getPrecioOriginal());
        entity.setPorcentajeDescuento(dto.getPorcentajeDescuento() != null ? dto.getPorcentajeDescuento() : BigDecimal.ZERO);
        entity.setDuracionDias(dto.getDuracionDias());
        entity.setNumeroSolicitudesIncluidas(dto.getNumeroSolicitudesIncluidas());
        entity.setSoportePrioritario(dto.getSoportePrioritario() != null ? dto.getSoportePrioritario() : false);
        entity.setDestacado(dto.getDestacado() != null ? dto.getDestacado() : false);
        entity.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        entity.setOrdenVisualizacion(dto.getOrdenVisualizacion());
        entity.setColorHexadecimal(dto.getColorHexadecimal());
        entity.setIcono(dto.getIcono());
        entity.setCreadoPor(dto.getCreadoPor());
    }

    private void mapUpdateDTOToEntity(UpdateSuscripcionDTO dto, Suscripcion entity) {
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            entity.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            entity.setDescripcion(dto.getDescripcion());
        }
        if (dto.getPrecioOriginal() != null) {
            entity.setPrecioOriginal(dto.getPrecioOriginal());
        }
        if (dto.getPorcentajeDescuento() != null) {
            entity.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        }
        if (dto.getDuracionDias() != null) {
            entity.setDuracionDias(dto.getDuracionDias());
        }
        if (dto.getNumeroSolicitudesIncluidas() != null) {
            entity.setNumeroSolicitudesIncluidas(dto.getNumeroSolicitudesIncluidas());
        }
        if (dto.getSoportePrioritario() != null) {
            entity.setSoportePrioritario(dto.getSoportePrioritario());
        }
        if (dto.getDestacado() != null) {
            entity.setDestacado(dto.getDestacado());
        }
        if (dto.getActivo() != null) {
            entity.setActivo(dto.getActivo());
        }
        if (dto.getOrdenVisualizacion() != null) {
            entity.setOrdenVisualizacion(dto.getOrdenVisualizacion());
        }
        if (dto.getColorHexadecimal() != null) {
            entity.setColorHexadecimal(dto.getColorHexadecimal());
        }
        if (dto.getIcono() != null) {
            entity.setIcono(dto.getIcono());
        }
        if (dto.getModificadoPor() != null) {
            entity.setModificadoPor(dto.getModificadoPor());
        }
    }
}
