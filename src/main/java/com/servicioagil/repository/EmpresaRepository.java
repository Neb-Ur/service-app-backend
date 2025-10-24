package com.servicioagil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    // Buscar por usuario
    Optional<Empresa> findByUsuarioId(Long usuarioId);
    
    // Buscar por RUT (equivalente a RFC)
    Optional<Empresa> findByRut(String rut);
    
    // Buscar empresas activas
    List<Empresa> findByActivoTrue();
    
    // Búsqueda con filtros básicos (adaptado a la estructura real)
    @Query("SELECT e FROM Empresa e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:rut IS NULL OR e.rut = :rut) AND " +
           "(:activo IS NULL OR e.activo = :activo)")
    Page<Empresa> findWithFilters(@Param("nombre") String nombre,
                                  @Param("rut") String rut,
                                  @Param("activo") Boolean activo,
                                  Pageable pageable);
    
    // Empresas con más técnicos
    @Query("SELECT e FROM Empresa e LEFT JOIN e.tecnicos t WHERE e.activo = true GROUP BY e ORDER BY COUNT(t) DESC")
    List<Empresa> findTopByTecnicosCount(Pageable pageable);
}