package com.servicioagil.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    
    // Buscar por usuario
    Optional<Tecnico> findByUsuarioId(Long usuarioId);
    
    // Buscar por empresa
    List<Tecnico> findByEmpresaId(Long empresaId);
    
    // Técnicos disponibles
    List<Tecnico> findByDisponibleTrue();
    
    // Técnicos verificados
    List<Tecnico> findByVerificadoTrue();
    
    // Técnicos por subcategoría
    @Query("SELECT t FROM Tecnico t JOIN t.subcategoria s WHERE s.id = :subcategoriaId")
    List<Tecnico> findBySubcategoriaId(@Param("subcategoriaId") Long subcategoriaId);
    
    // Técnicos activos por subcategoría
    @Query("SELECT t FROM Tecnico t JOIN t.subcategoria s WHERE s.id = :subcategoriaId AND t.activo = :activo")
    List<Tecnico> findBySubcategoriaIdAndActivoTrue(@Param("subcategoriaId") Long subcategoriaId);
    
    // Búsqueda con filtros
    @Query("SELECT DISTINCT t FROM Tecnico t LEFT JOIN t.subcategoria s WHERE " +
           "(:subcategoriaId IS NULL OR s.id = :subcategoriaId) AND " +
           "(:empresaId IS NULL OR t.empresa.id = :empresaId) AND " +
           "(:clasificacion IS NULL OR t.clasificacion = :clasificacion) AND " +
           "(:disponible IS NULL OR t.disponible = :disponible) AND " +
           "(:verificado IS NULL OR t.verificado = :verificado) AND " +
           "(:radioCobertura IS NULL OR t.radioCobertura >= :radioCobertura) AND " +
           "(:calificacionMinima IS NULL OR t.ratingPromedio >= :calificacionMinima)")
    Page<Tecnico> findWithFilters(@Param("subcategoriaId") Long subcategoriaId,
                                  @Param("empresaId") Long empresaId,
                                  @Param("clasificacion") Integer clasificacion,
                                  @Param("disponible") Boolean disponible,
                                  @Param("verificado") Boolean verificado,
                                  @Param("radioCobertura") Integer radioCobertura,
                                  @Param("calificacionMinima") BigDecimal calificacionMinima,
                                  Pageable pageable);
    
    // Top técnicos por calificación
    List<Tecnico> findTop10ByOrderByRatingPromedioDesc();
    
    // Técnicos por rango de tarifa
    @Query("SELECT t FROM Tecnico t WHERE t.precioBaseHora BETWEEN :tarifaMin AND :tarifaMax")
    List<Tecnico> findByTarifaRange(@Param("tarifaMin") BigDecimal tarifaMin, 
                                    @Param("tarifaMax") BigDecimal tarifaMax);
    
    // Contar técnicos por clasificación
    @Query("SELECT t.clasificacion, COUNT(t) FROM Tecnico t GROUP BY t.clasificacion")
    List<Object[]> countByClasificacion();
}