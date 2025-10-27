package com.servicioagil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicioagil.dto.EmergenciaRequestDTO;
import com.servicioagil.dto.EmergenciaResponseDTO;
import com.servicioagil.dto.NotificacionRespuestaDTO;
import com.servicioagil.entity.EmergenciaNotificacion;
import com.servicioagil.entity.EmergenciaNotificacion.EstadoNotificacion;
import com.servicioagil.entity.Solicitud;
import com.servicioagil.entity.Solicitud.EstadoSolicitud;
import com.servicioagil.entity.Solicitud.PrioridadSolicitud;
import com.servicioagil.entity.Subcategoria;
import com.servicioagil.entity.Tecnico;
import com.servicioagil.entity.Usuario;
import com.servicioagil.exception.ResourceNotFoundException;
import com.servicioagil.repository.EmergenciaNotificacionRepository;
import com.servicioagil.repository.SolicitudRepository;
import com.servicioagil.repository.SubcategoriaRepository;
import com.servicioagil.repository.TecnicoRepository;
import com.servicioagil.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergenciaService {

    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final EmergenciaNotificacionRepository emergenciaNotificacionRepository;
    
    private static final int TIMEOUT_SEGUNDOS = 90;
    private static final double RADIO_INICIAL_KM = 5.0;
    private static final double INCREMENTO_RADIO_KM = 2.0;
    private static final int MAX_RADIO_KM = 50;

    /**
     * Crear una nueva solicitud de emergencia y buscar técnicos cercanos
     */
    @Transactional
    public EmergenciaResponseDTO crearEmergencia(EmergenciaRequestDTO request) {
        log.info("Creando emergencia para usuario: {}", request.getUsuarioId());

        // Validar usuario
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validar subcategoría
        Subcategoria subcategoria = subcategoriaRepository.findById(request.getSubcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        // Crear solicitud de emergencia
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario(usuario);
        solicitud.setSubcategoria(subcategoria);
        solicitud.setTitulo("EMERGENCIA: " + subcategoria.getNombre());
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setDireccionServicio(request.getDireccion());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setPrioridad(PrioridadSolicitud.URGENTE);
        solicitud.setEsUrgente(true);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setObservaciones(request.getNotas());

        solicitud = solicitudRepository.save(solicitud);
        log.info("Solicitud de emergencia creada con ID: {}", solicitud.getId());

        // Buscar técnicos cercanos y enviar notificaciones
        buscarYNotificarTecnicos(solicitud, request.getLatitud(), request.getLongitud(), RADIO_INICIAL_KM);

        return construirRespuesta(solicitud);
    }

    /**
     * Buscar técnicos cercanos y enviarles notificaciones
     */
    @Transactional
    public void buscarYNotificarTecnicos(Solicitud solicitud, Double latitud, Double longitud, Double radioKm) {
        log.info("Buscando técnicos cercanos en radio de {} km", radioKm);

        List<Object[]> resultados = tecnicoRepository.findTecnicosCercanos(
                latitud, 
                longitud, 
                solicitud.getSubcategoria().getId(),
                radioKm
        );

        if (resultados.isEmpty()) {
            log.warn("No se encontraron técnicos en radio de {} km", radioKm);
            
            // Si no se encuentra nadie y no hemos alcanzado el límite, aumentar el radio
            if (radioKm < MAX_RADIO_KM) {
                double nuevoRadio = radioKm + INCREMENTO_RADIO_KM;
                log.info("Ampliando búsqueda a {} km", nuevoRadio);
                buscarYNotificarTecnicos(solicitud, latitud, longitud, nuevoRadio);
            }
            return;
        }

        // Enviar notificaciones a los técnicos encontrados
        int orden = 1;
        for (Object[] resultado : resultados) {
            Tecnico tecnico = tecnicoRepository.findById(((Number) resultado[0]).longValue())
                    .orElse(null);
            
            if (tecnico != null) {
                Double distancia = ((Number) resultado[resultado.length - 1]).doubleValue() * 1000; // Convertir a metros
                
                EmergenciaNotificacion notificacion = new EmergenciaNotificacion();
                notificacion.setSolicitud(solicitud);
                notificacion.setTecnico(tecnico);
                notificacion.setEstado(EstadoNotificacion.PENDIENTE);
                notificacion.setFechaEnvio(LocalDateTime.now());
                notificacion.setTimeoutEn(LocalDateTime.now().plusSeconds(TIMEOUT_SEGUNDOS));
                notificacion.setDistanciaMetros(distancia);
                notificacion.setOrdenContacto(orden++);

                emergenciaNotificacionRepository.save(notificacion);
                log.info("Notificación enviada al técnico {} (distancia: {} m)", tecnico.getId(), distancia);
                
                // TODO: Enviar notificación push real aquí
                enviarNotificacionPush(tecnico, solicitud, distancia);
            }
        }
    }

    /**
     * Procesar respuesta del técnico a la notificación de emergencia
     */
    @Transactional
    public EmergenciaResponseDTO procesarRespuestaTecnico(NotificacionRespuestaDTO respuesta) {
        log.info("Procesando respuesta del técnico {} para notificación {}", 
                respuesta.getTecnicoId(), respuesta.getNotificacionId());

        EmergenciaNotificacion notificacion = emergenciaNotificacionRepository
                .findById(respuesta.getNotificacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));

        Solicitud solicitud = notificacion.getSolicitud();

        if (respuesta.getAceptar()) {
            // Técnico acepta la emergencia
            notificacion.setEstado(EstadoNotificacion.ACEPTADA);
            notificacion.setFechaRespuesta(LocalDateTime.now());
            
            // Asignar técnico a la solicitud
            solicitud.setTecnico(notificacion.getTecnico());
            solicitud.setEstado(EstadoSolicitud.ASIGNADA);
            solicitud.setFechaInicioTrabajo(LocalDateTime.now());
            
            solicitudRepository.save(solicitud);
            emergenciaNotificacionRepository.save(notificacion);
            
            // Cancelar otras notificaciones pendientes
            cancelarOtrasNotificaciones(solicitud.getId(), notificacion.getId());
            
            // Notificar al cliente que fue aceptado
            notificarClienteAceptacion(solicitud, notificacion.getTecnico());
            
            log.info("Emergencia aceptada por técnico: {}", respuesta.getTecnicoId());
        } else {
            // Técnico rechaza la emergencia
            notificacion.setEstado(EstadoNotificacion.RECHAZADA);
            notificacion.setFechaRespuesta(LocalDateTime.now());
            emergenciaNotificacionRepository.save(notificacion);
            
            log.info("Emergencia rechazada por técnico: {}", respuesta.getTecnicoId());
            
            // Buscar el siguiente técnico más cercano
            buscarSiguienteTecnico(solicitud);
        }

        return construirRespuesta(solicitud);
    }

    /**
     * Buscar el siguiente técnico disponible más cercano
     */
    @Transactional
    public void buscarSiguienteTecnico(Solicitud solicitud) {
        log.info("Buscando siguiente técnico para solicitud: {}", solicitud.getId());
        
        // Obtener todas las notificaciones de esta solicitud
        List<EmergenciaNotificacion> notificaciones = emergenciaNotificacionRepository
                .findBySolicitudIdOrderByOrdenContactoAsc(solicitud.getId());
        
        // Encontrar el siguiente técnico que no haya sido contactado
        List<EmergenciaNotificacion> noContactados = notificaciones.stream()
                .filter(n -> n.getEstado() == EstadoNotificacion.PENDIENTE 
                        && n.getTimeoutEn().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        
        if (noContactados.isEmpty()) {
            log.warn("No hay más técnicos disponibles para contactar");
            // Aquí se podría ampliar el radio de búsqueda
            Double latitud = solicitud.getUsuario().getLatitude();
            Double longitud = solicitud.getUsuario().getLongitude();
            
            if (latitud != null && longitud != null) {
                buscarYNotificarTecnicos(solicitud, latitud, longitud, RADIO_INICIAL_KM + INCREMENTO_RADIO_KM);
            }
        }
    }

    /**
     * Cancelar todas las notificaciones pendientes excepto la aceptada
     */
    @Transactional
    public void cancelarOtrasNotificaciones(Long solicitudId, Long notificacionAceptadaId) {
        List<EmergenciaNotificacion> notificaciones = emergenciaNotificacionRepository
                .findBySolicitudIdAndEstado(solicitudId, EstadoNotificacion.PENDIENTE);
        
        for (EmergenciaNotificacion notif : notificaciones) {
            if (!notif.getId().equals(notificacionAceptadaId)) {
                notif.setEstado(EstadoNotificacion.CANCELADA);
                emergenciaNotificacionRepository.save(notif);
            }
        }
        
        log.info("Canceladas {} notificaciones pendientes", notificaciones.size());
    }

    /**
     * Tarea programada para verificar timeouts de notificaciones
     */
    @Scheduled(fixedDelay = 30000) // Cada 30 segundos
    @Transactional
    public void verificarTimeouts() {
        List<EmergenciaNotificacion> vencidas = emergenciaNotificacionRepository
                .findNotificacionesVencidas(EstadoNotificacion.PENDIENTE, LocalDateTime.now());
        
        for (EmergenciaNotificacion notificacion : vencidas) {
            notificacion.setEstado(EstadoNotificacion.TIMEOUT);
            emergenciaNotificacionRepository.save(notificacion);
            
            log.info("Notificación {} marcada como TIMEOUT", notificacion.getId());
            
            // Buscar siguiente técnico
            buscarSiguienteTecnico(notificacion.getSolicitud());
        }
    }

    /**
     * Obtener estado de una emergencia
     */
    public EmergenciaResponseDTO obtenerEstadoEmergencia(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));
        
        return construirRespuesta(solicitud);
    }

    /**
     * Obtener notificaciones pendientes para un técnico
     */
    public List<EmergenciaNotificacion> obtenerNotificacionesPendientes(Long tecnicoId) {
        return emergenciaNotificacionRepository.findByTecnicoIdAndEstado(
                tecnicoId, EstadoNotificacion.PENDIENTE);
    }

    /**
     * Construir respuesta DTO
     */
    private EmergenciaResponseDTO construirRespuesta(Solicitud solicitud) {
        List<EmergenciaNotificacion> notificaciones = emergenciaNotificacionRepository
                .findBySolicitudIdOrderByOrdenContactoAsc(solicitud.getId());

        EmergenciaResponseDTO.TecnicoEmergenciaDTO tecnicoDTO = null;
        if (solicitud.getTecnico() != null) {
            Tecnico tecnico = solicitud.getTecnico();
            Usuario usuarioTecnico = tecnico.getUsuario();
            
            tecnicoDTO = EmergenciaResponseDTO.TecnicoEmergenciaDTO.builder()
                    .id(tecnico.getId())
                    .nombre(usuarioTecnico.getNombre())
                    .apellido(usuarioTecnico.getApellido())
                    .telefono(usuarioTecnico.getTelefono())
                    .latitud(usuarioTecnico.getLatitude())
                    .longitud(usuarioTecnico.getLongitude())
                    .estado("EN_CAMINO")
                    .build();
        }

        List<EmergenciaResponseDTO.NotificacionDTO> notificacionesDTO = notificaciones.stream()
                .map(n -> EmergenciaResponseDTO.NotificacionDTO.builder()
                        .id(n.getId())
                        .tecnicoId(n.getTecnico().getId())
                        .tecnicoNombre(n.getTecnico().getUsuario().getNombre() + " " + 
                                      n.getTecnico().getUsuario().getApellido())
                        .estado(n.getEstado().name())
                        .fechaEnvio(n.getFechaEnvio())
                        .fechaRespuesta(n.getFechaRespuesta())
                        .timeoutEn(n.getTimeoutEn())
                        .distanciaMetros(n.getDistanciaMetros())
                        .ordenContacto(n.getOrdenContacto())
                        .build())
                .collect(Collectors.toList());

        return EmergenciaResponseDTO.builder()
                .solicitudId(solicitud.getId())
                .estado(solicitud.getEstado().name())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .descripcion(solicitud.getDescripcion())
                .direccion(solicitud.getDireccionServicio())
                .latitud(solicitud.getUsuario().getLatitude())
                .longitud(solicitud.getUsuario().getLongitude())
                .tecnicoAsignado(tecnicoDTO)
                .notificaciones(notificacionesDTO)
                .build();
    }

    /**
     * Enviar notificación push al técnico (placeholder)
     */
    @Async
    private void enviarNotificacionPush(Tecnico tecnico, Solicitud solicitud, Double distancia) {
        // TODO: Implementar con Firebase Cloud Messaging o servicio similar
        log.info("📱 Enviando notificación push al técnico {} para emergencia {}", 
                tecnico.getId(), solicitud.getId());
    }

    /**
     * Notificar al cliente que la emergencia fue aceptada
     */
    @Async
    private void notificarClienteAceptacion(Solicitud solicitud, Tecnico tecnico) {
        // TODO: Implementar con Firebase Cloud Messaging o servicio similar
        log.info("✅ Notificando al cliente {} que técnico {} aceptó la emergencia", 
                solicitud.getUsuario().getId(), tecnico.getId());
    }
}
