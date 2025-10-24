package com.servicioagil.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    
    // Reseñas por técnico
    List<Resena> findByTecnicoId(Long tecnicoId);
    
    // Reseñas por usuario (cliente)
    List<Resena> findByUsuarioId(Long usuarioId);
    
    // Reseñas por solicitud
    List<Resena> findBySolicitudId(Long solicitudId);
    
    // Reseñas por rango de calificación
    List<Resena> findByCalificacionBetween(BigDecimal minCalificacion, BigDecimal maxCalificacion);
    
    // Reseñas recientes de un técnico
    List<Resena> findTop5ByTecnicoIdOrderByFechaCreacionDesc(Long tecnicoId);
    
    // Promedio de calificación por técnico
    @Query("SELECT AVG(r.calificacion) FROM Resena r WHERE r.tecnico.id = :tecnicoId")
    BigDecimal getAverageCalificacionByTecnico(@Param("tecnicoId") Long tecnicoId);
    
    // Contar reseñas por técnico
    @Query("SELECT COUNT(r) FROM Resena r WHERE r.tecnico.id = :tecnicoId")
    Long countByTecnicoId(@Param("tecnicoId") Long tecnicoId);
    
    // Distribución de calificaciones por técnico
    @Query("SELECT r.calificacion, COUNT(r) FROM Resena r WHERE r.tecnico.id = :tecnicoId GROUP BY r.calificacion ORDER BY r.calificacion DESC")
    List<Object[]> getCalificacionDistributionByTecnico(@Param("tecnicoId") Long tecnicoId);
    
    // Búsqueda con filtros
    @Query("SELECT r FROM Resena r WHERE " +
           "(:tecnicoId IS NULL OR r.tecnico.id = :tecnicoId) AND " +
           "(:usuarioId IS NULL OR r.usuario.id = :usuarioId) AND " +
           "(:calificacionMin IS NULL OR r.calificacion >= :calificacionMin) AND " +
           "(:calificacionMax IS NULL OR r.calificacion <= :calificacionMax) AND " +
           "(:tieneComentario IS NULL OR (:tieneComentario = true AND r.comentario IS NOT NULL) OR (:tieneComentario = false AND r.comentario IS NULL))")
    Page<Resena> findWithFilters(@Param("tecnicoId") Long tecnicoId,
                                 @Param("usuarioId") Long usuarioId,
                                 @Param("calificacionMin") BigDecimal calificacionMin,
                                 @Param("calificacionMax") BigDecimal calificacionMax,
                                 @Param("tieneComentario") Boolean tieneComentario,
                                 Pageable pageable);
    
    // Top reseñas (mejor calificadas)
    List<Resena> findTop10ByOrderByCalificacionDescFechaCreacionDesc();

}