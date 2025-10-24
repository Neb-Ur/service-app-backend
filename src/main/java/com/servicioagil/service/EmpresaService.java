package com.servicioagil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicioagil.dto.CreateEmpresaDTO;
import com.servicioagil.dto.EmpresaDTO;
import com.servicioagil.dto.UpdateEmpresaDTO;
import com.servicioagil.entity.Empresa;
import com.servicioagil.entity.Usuario;
import com.servicioagil.exception.BusinessException;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.EmpresaRepository;
import com.servicioagil.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<EmpresaDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EmpresaDTO> findWithFilters(String nombre, String rut, Boolean activo, Pageable pageable) {
        return empresaRepository.findWithFilters(nombre, rut, activo, pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO findById(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        return convertToDTO(empresa);
    }

    @Transactional(readOnly = true)
    public EmpresaDTO findByUsuarioId(Long usuarioId) {
        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró empresa para el usuario con ID: " + usuarioId));
        return convertToDTO(empresa);
    }

    @Transactional(readOnly = true)
    public List<EmpresaDTO> findByActiva(boolean activa) {
        return empresaRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmpresaDTO create(CreateEmpresaDTO createEmpresaDTO, Long usuarioId) {
        // Verificar que el usuario existe y es de tipo EMPRESA
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        if (usuario.getTipoUsuario() != Usuario.TipoUsuario.EMPRESA) {
            throw new BusinessException("El usuario debe ser de tipo EMPRESA para crear una empresa");
        }

        // Verificar que el usuario no tenga ya una empresa
        if (empresaRepository.findByUsuarioId(usuarioId).isPresent()) {
            throw new BusinessException("El usuario ya tiene una empresa asociada");
        }

        Empresa empresa = new Empresa();
        empresa.setNombre(createEmpresaDTO.getRazonSocial()); // Mapear razonSocial a nombre
        empresa.setRut(createEmpresaDTO.getRfc()); // Mapear RFC a RUT
        empresa.setDescripcion(createEmpresaDTO.getDescripcion());
        empresa.setDireccion(createEmpresaDTO.getDireccion());
        empresa.setTelefono(createEmpresaDTO.getTelefono());
        empresa.setSitioWeb(createEmpresaDTO.getSitioWeb());
        empresa.setUsuario(usuario);
        empresa.setActivo(createEmpresaDTO.getActiva() != null ? createEmpresaDTO.getActiva() : true);

        Empresa savedEmpresa = empresaRepository.save(empresa);
        return convertToDTO(savedEmpresa);
    }

    @Transactional
    public EmpresaDTO update(Long id, UpdateEmpresaDTO updateEmpresaDTO) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));

        // Verificar RUT único (si se está cambiando)
        if (updateEmpresaDTO.getRfc() != null && !updateEmpresaDTO.getRfc().equals(empresa.getRut())) {
            if (empresaRepository.findByRut(updateEmpresaDTO.getRfc()).isPresent()) {
                throw new BusinessException("Ya existe una empresa con el RFC: " + updateEmpresaDTO.getRfc());
            }
        }

        // Actualizar campos no nulos
        if (updateEmpresaDTO.getRazonSocial() != null) {
            empresa.setNombre(updateEmpresaDTO.getRazonSocial());
        }
        if (updateEmpresaDTO.getRfc() != null) {
            empresa.setRut(updateEmpresaDTO.getRfc());
        }
        if (updateEmpresaDTO.getTelefono() != null) {
            empresa.setTelefono(updateEmpresaDTO.getTelefono());
        }
        if (updateEmpresaDTO.getDireccion() != null) {
            empresa.setDireccion(updateEmpresaDTO.getDireccion());
        }
        if (updateEmpresaDTO.getSitioWeb() != null) {
            empresa.setSitioWeb(updateEmpresaDTO.getSitioWeb());
        }
        if (updateEmpresaDTO.getDescripcion() != null) {
            empresa.setDescripcion(updateEmpresaDTO.getDescripcion());
        }
        if (updateEmpresaDTO.getActiva() != null) {
            empresa.setActivo(updateEmpresaDTO.getActiva());
        }

        Empresa updatedEmpresa = empresaRepository.save(empresa);
        return convertToDTO(updatedEmpresa);
    }

    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        
        // Soft delete - marcar como inactiva
        empresa.setActivo(false);
        empresaRepository.save(empresa);
    }

    private EmpresaDTO convertToDTO(Empresa empresa) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(empresa.getId());
        dto.setRazonSocial(empresa.getNombre());
        dto.setRfc(empresa.getRut());
        dto.setDescripcion(empresa.getDescripcion());
        dto.setDireccion(empresa.getDireccion());
        dto.setTelefono(empresa.getTelefono());
        dto.setSitioWeb(empresa.getSitioWeb());
        dto.setActiva(empresa.getActivo());
        dto.setFechaCreacion(empresa.getFechaCreacion());
        dto.setFechaActualizacion(empresa.getFechaModificacion());
        
        // Mapear información del usuario
        if (empresa.getUsuario() != null) {
            dto.setUsuarioId(empresa.getUsuario().getId());
            dto.setUsuarioNombre(empresa.getUsuario().getNombre());
            dto.setUsuarioEmail(empresa.getUsuario().getEmail());
        }
        
        return dto;
    }
}