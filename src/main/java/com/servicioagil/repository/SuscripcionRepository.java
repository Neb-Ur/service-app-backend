package com.servicioagil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Suscripcion;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    // Buscar suscripciones activas
    List<Suscripcion> findByActivoTrueOrderByOrdenVisualizacionAsc();

    // Buscar suscripciones destacadas
    List<Suscripcion> findByDestacadoTrueAndActivoTrueOrderByOrdenVisualizacionAsc();

    // Buscar suscripción por nombre
    Optional<Suscripcion> findByNombreIgnoreCase(String nombre);

    // Verificar si existe una suscripción con el mismo nombre (excluyendo un ID específico)
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    // Verificar si existe una suscripción con el mismo nombre
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar suscripciones por rango de precio
    @Query("SELECT s FROM Suscripcion s WHERE s.activo = true AND s.precioFinal BETWEEN :minPrecio AND :maxPrecio ORDER BY s.precioFinal ASC")
    List<Suscripcion> findByPrecioFinalBetween(java.math.BigDecimal minPrecio, java.math.BigDecimal maxPrecio);

    // Buscar suscripciones con descuento
    @Query("SELECT s FROM Suscripcion s WHERE s.activo = true AND s.porcentajeDescuento > 0 ORDER BY s.porcentajeDescuento DESC")
    List<Suscripcion> findSuscripcionesConDescuento();

    // Contar suscripciones activas
    long countByActivoTrue();
}
