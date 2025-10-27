package com.servicioagil.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicioagil.dto.ProveedorServicioDTO;
import com.servicioagil.entity.Empresa;
import com.servicioagil.entity.Tecnico;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.EmpresaRepository;
import com.servicioagil.repository.SubcategoriaRepository;
import com.servicioagil.repository.TecnicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorServicioService {

    private final TecnicoRepository tecnicoRepository;
    private final EmpresaRepository empresaRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    @Transactional(readOnly = true)
    public List<ProveedorServicioDTO> getProveedoresPorSubcategoria(Long subcategoriaId) {
        log.info("Obteniendo proveedores de servicio para subcategoría con id: {}", subcategoriaId);

        // Verificar que la subcategoría existe
        if (!subcategoriaRepository.existsById(subcategoriaId)) {
            throw new ResourceNotFoundException("Subcategoría no encontrada con id: " + subcategoriaId);
        }

        List<ProveedorServicioDTO> proveedores = new ArrayList<>();

        // Obtener técnicos independientes de la subcategoría
        List<Tecnico> tecnicos = tecnicoRepository.findBySubcategoriaId(subcategoriaId);
        for (Tecnico tecnico : tecnicos) {
            // Solo agregar técnicos que no pertenecen a ninguna empresa
            if (tecnico.getEmpresa() == null && tecnico.getDisponible()) {
                proveedores.add(convertTecnicoToDTO(tecnico));
            }
        }

        // Obtener empresas que tienen técnicos en esta subcategoría
        List<Empresa> empresas = empresaRepository.findByActivoTrue();
        for (Empresa empresa : empresas) {
            // Verificar si la empresa tiene al menos un técnico en esta subcategoría
            boolean tieneTenicosEnSubcategoria = empresa.getTecnicos().stream()
                    .anyMatch(t -> t.getSubcategoria() != null && 
                                 t.getSubcategoria().getId().equals(subcategoriaId) &&
                                 t.getActivo() && 
                                 t.getDisponible());
            
            if (tieneTenicosEnSubcategoria) {
                proveedores.add(convertEmpresaToDTO(empresa, subcategoriaId));
            }
        }

        log.info("Se encontraron {} proveedores para la subcategoría {}", proveedores.size(), subcategoriaId);
        return proveedores;
    }

    private ProveedorServicioDTO convertTecnicoToDTO(Tecnico tecnico) {
        ProveedorServicioDTO dto = new ProveedorServicioDTO();
        dto.setId(tecnico.getId());
        dto.setNombre(tecnico.getUsuario() != null ? tecnico.getUsuario().getNombre() : "Sin nombre");
        dto.setEsEmpresa(false);
        dto.setPrecioBase(tecnico.getPrecioBaseHora());
        dto.setFoto(tecnico.getUsuario() != null ? tecnico.getUsuario().getEmail() : null); // Ajustar si hay campo foto
        dto.setDescripcion(tecnico.getDescripcionProfesional());
        dto.setInformacionAdicional(buildTecnicoInfo(tecnico));
        dto.setCalificacionPromedio(tecnico.getRatingPromedio() != null ? tecnico.getRatingPromedio().doubleValue() : 0.0);
        dto.setNumeroResenas(tecnico.getTotalResenas());
        dto.setTelefono(null); // Los técnicos no tienen teléfono directo, usar el del usuario si existe
        dto.setEmail(tecnico.getUsuario() != null ? tecnico.getUsuario().getEmail() : null);
        dto.setDireccion(null);
        dto.setDisponible(tecnico.getDisponible());
        dto.setSubcategoriaNombre(tecnico.getSubcategoria() != null ? tecnico.getSubcategoria().getNombre() : null);
        dto.setSubcategoriaId(tecnico.getSubcategoria() != null ? tecnico.getSubcategoria().getId() : null);
        return dto;
    }

    private ProveedorServicioDTO convertEmpresaToDTO(Empresa empresa, Long subcategoriaId) {
        ProveedorServicioDTO dto = new ProveedorServicioDTO();
        dto.setId(empresa.getId());
        dto.setNombre(empresa.getNombre());
        dto.setEsEmpresa(true);
        
        // Calcular precio base promedio de los técnicos en esta subcategoría
        dto.setPrecioBase(calcularPrecioPromedioEmpresa(empresa, subcategoriaId));
        
        dto.setFoto(null); // Ajustar si Empresa tiene campo foto
        dto.setDescripcion(empresa.getDescripcion());
        dto.setInformacionAdicional(buildEmpresaInfo(empresa, subcategoriaId));
        
        // Calcular calificación promedio de los técnicos en esta subcategoría
        dto.setCalificacionPromedio(calcularCalificacionPromedioEmpresa(empresa, subcategoriaId));
        dto.setNumeroResenas(calcularTotalResenasEmpresa(empresa, subcategoriaId));
        
        dto.setTelefono(empresa.getTelefono());
        dto.setEmail(empresa.getUsuario() != null ? empresa.getUsuario().getEmail() : null);
        dto.setDireccion(empresa.getDireccion());
        dto.setDisponible(empresa.getActivo());
        
        // Obtener el nombre de la subcategoría
        dto.setSubcategoriaNombre(subcategoriaRepository.findById(subcategoriaId)
                .map(s -> s.getNombre())
                .orElse(null));
        dto.setSubcategoriaId(subcategoriaId);
        
        return dto;
    }

    private String buildTecnicoInfo(Tecnico tecnico) {
        StringBuilder info = new StringBuilder();
        if (tecnico.getAnosExperiencia() != null) {
            info.append("Años de experiencia: ").append(tecnico.getAnosExperiencia()).append(". ");
        }
        if (tecnico.getTotalTrabajosCompletados() != null && tecnico.getTotalTrabajosCompletados() > 0) {
            info.append("Trabajos completados: ").append(tecnico.getTotalTrabajosCompletados()).append(". ");
        }
        if (tecnico.getCertificados() != null && !tecnico.getCertificados().isEmpty()) {
            info.append("Certificaciones: ").append(tecnico.getCertificados()).append(". ");
        }
        if (tecnico.getRadioCobertura() != null) {
            info.append("Radio de cobertura: ").append(tecnico.getRadioCobertura()).append(" km.");
        }
        return info.toString();
    }

    private String buildEmpresaInfo(Empresa empresa, Long subcategoriaId) {
        long numeroTecnicos = empresa.getTecnicos().stream()
                .filter(t -> t.getSubcategoria() != null && 
                           t.getSubcategoria().getId().equals(subcategoriaId) &&
                           t.getActivo() && 
                           t.getDisponible())
                .count();
        
        StringBuilder info = new StringBuilder();
        info.append("Técnicos disponibles en esta categoría: ").append(numeroTecnicos).append(". ");
        if (empresa.getRut() != null && !empresa.getRut().isEmpty()) {
            info.append("RUT: ").append(empresa.getRut()).append(". ");
        }
        if (empresa.getSitioWeb() != null && !empresa.getSitioWeb().isEmpty()) {
            info.append("Sitio web: ").append(empresa.getSitioWeb()).append(".");
        }
        return info.toString();
    }

    private java.math.BigDecimal calcularPrecioPromedioEmpresa(Empresa empresa, Long subcategoriaId) {
        return empresa.getTecnicos().stream()
                .filter(t -> t.getSubcategoria() != null && 
                           t.getSubcategoria().getId().equals(subcategoriaId) &&
                           t.getActivo() && 
                           t.getDisponible() &&
                           t.getPrecioBaseHora() != null)
                .map(Tecnico::getPrecioBaseHora)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                .divide(java.math.BigDecimal.valueOf(
                    empresa.getTecnicos().stream()
                        .filter(t -> t.getSubcategoria() != null && 
                                   t.getSubcategoria().getId().equals(subcategoriaId) &&
                                   t.getActivo() && 
                                   t.getDisponible() &&
                                   t.getPrecioBaseHora() != null)
                        .count()
                ), 2, java.math.RoundingMode.HALF_UP);
    }

    private Double calcularCalificacionPromedioEmpresa(Empresa empresa, Long subcategoriaId) {
        return empresa.getTecnicos().stream()
                .filter(t -> t.getSubcategoria() != null && 
                           t.getSubcategoria().getId().equals(subcategoriaId) &&
                           t.getActivo() &&
                           t.getRatingPromedio() != null)
                .mapToDouble(t -> t.getRatingPromedio().doubleValue())
                .average()
                .orElse(0.0);
    }

    private Integer calcularTotalResenasEmpresa(Empresa empresa, Long subcategoriaId) {
        return empresa.getTecnicos().stream()
                .filter(t -> t.getSubcategoria() != null && 
                           t.getSubcategoria().getId().equals(subcategoriaId) &&
                           t.getActivo())
                .mapToInt(t -> t.getTotalResenas() != null ? t.getTotalResenas() : 0)
                .sum();
    }
}
