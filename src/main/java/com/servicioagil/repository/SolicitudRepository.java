package com.servicioagil.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Solicitud;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    
    // Solicitudes por técnico
    List<Solicitud> findByTecnicoId(Long tecnicoId);
    
    // Solicitudes por estado
    List<Solicitud> findByEstado(Solicitud.EstadoSolicitud estado);
    
    // Solicitudes por prioridad
    List<Solicitud> findByPrioridad(Solicitud.PrioridadSolicitud prioridad);
    
    // Solicitudes por subcategoría
    List<Solicitud> findBySubcategoriaId(Long subcategoriaId);
    
    // Solicitudes pendientes de asignación
    List<Solicitud> findByEstadoAndTecnicoIsNull(Solicitud.EstadoSolicitud estado);
    
    // Solicitudes por rango de fechas
    @Query("SELECT s FROM Solicitud s WHERE s.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Solicitud> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio,
                                     @Param("fechaFin") LocalDateTime fechaFin);
    
    // Estadísticas por estado
    @Query("SELECT s.estado, COUNT(s) FROM Solicitud s GROUP BY s.estado")
    List<Object[]> countByEstado();
    
    // Solicitudes urgentes sin asignar
    @Query("SELECT s FROM Solicitud s WHERE s.prioridad = 'URGENTE' AND s.tecnico IS NULL ORDER BY s.fechaCreacion ASC")
    List<Solicitud> findUrgentUnassigned();
}