package com.servicioagil.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.EmergenciaNotificacion;
import com.servicioagil.entity.EmergenciaNotificacion.EstadoNotificacion;

@Repository
public interface EmergenciaNotificacionRepository extends JpaRepository<EmergenciaNotificacion, Long> {

    List<EmergenciaNotificacion> findBySolicitudIdOrderByOrdenContactoAsc(Long solicitudId);

    List<EmergenciaNotificacion> findByTecnicoIdAndEstado(Long tecnicoId, EstadoNotificacion estado);

    @Query("SELECT en FROM EmergenciaNotificacion en WHERE en.estado = :estado AND en.timeoutEn < :ahora")
    List<EmergenciaNotificacion> findNotificacionesVencidas(
        @Param("estado") EstadoNotificacion estado,
        @Param("ahora") LocalDateTime ahora
    );

    List<EmergenciaNotificacion> findBySolicitudIdAndEstado(Long solicitudId, EstadoNotificacion estado);
}
