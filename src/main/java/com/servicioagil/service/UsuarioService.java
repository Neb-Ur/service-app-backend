package com.servicioagil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicioagil.dto.CreateUsuarioDTO;
import com.servicioagil.dto.UpdateUsuarioDTO;
import com.servicioagil.dto.UsuarioDTO;
import com.servicioagil.entity.Usuario;
import com.servicioagil.exception.BusinessException;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        log.info("Obteniendo todos los usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return convertToDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        log.info("Obteniendo usuarios activos");
        List<Usuario> usuarios = usuarioRepository.findByActivoTrue();
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        log.info("Obteniendo usuarios por tipo: {}", tipoUsuario);
        List<Usuario> usuarios = usuarioRepository.findByTipoUsuario(tipoUsuario);
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarUsuariosPorNombre(String nombre) {
        log.info("Buscando usuarios por nombre: {}", nombre);
        List<Usuario> usuarios = usuarioRepository.findByNombreContainingIgnoreCase(nombre);
        return usuarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        log.info("Buscando usuario por email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return convertToDTO(usuario);
    }

    public UsuarioDTO crearUsuario(CreateUsuarioDTO createUsuarioDTO) {
        log.info("Creando nuevo usuario con email: {}", createUsuarioDTO.getEmail());
        
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(createUsuarioDTO.getEmail())) {
            throw new BusinessException("Ya existe un usuario con el email: " + createUsuarioDTO.getEmail());
        }

        Usuario usuario = modelMapper.map(createUsuarioDTO, Usuario.class);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        log.info("Usuario creado exitosamente con ID: {}", usuarioGuardado.getId());
        return convertToDTO(usuarioGuardado);
    }

    public UsuarioDTO actualizarUsuario(Long id, UpdateUsuarioDTO updateUsuarioDTO) {
        log.info("Actualizando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si el email ya existe (excluyendo el usuario actual)
        if (updateUsuarioDTO.getEmail() != null && 
            usuarioRepository.existsByEmailAndIdNot(updateUsuarioDTO.getEmail(), id)) {
            throw new BusinessException("Ya existe un usuario con el email: " + updateUsuarioDTO.getEmail());
        }

        // Actualizar solo los campos no nulos
        if (updateUsuarioDTO.getNombre() != null) {
            usuario.setNombre(updateUsuarioDTO.getNombre());
        }
        if (updateUsuarioDTO.getApellido() != null) {
            usuario.setApellido(updateUsuarioDTO.getApellido());
        }
        if (updateUsuarioDTO.getEmail() != null) {
            usuario.setEmail(updateUsuarioDTO.getEmail());
        }
        if (updateUsuarioDTO.getTelefono() != null) {
            usuario.setTelefono(updateUsuarioDTO.getTelefono());
        }
        if (updateUsuarioDTO.getDireccion() != null) {
            usuario.setDireccion(updateUsuarioDTO.getDireccion());
        }
        if (updateUsuarioDTO.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(updateUsuarioDTO.getFechaNacimiento());
        }
        if (updateUsuarioDTO.getActivo() != null) {
            usuario.setActivo(updateUsuarioDTO.getActivo());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        
        log.info("Usuario actualizado exitosamente con ID: {}", usuarioActualizado.getId());
        return convertToDTO(usuarioActualizado);
    }

    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        
        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    @Transactional(readOnly = true)
    public long contarUsuariosInactivos() {
        return usuarioRepository.countByActivoFalse();
    }

    @Transactional(readOnly = true)
    public long contarUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.countByTipoUsuario(tipoUsuario);
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
        return dto;
    }
}