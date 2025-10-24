package com.servicioagil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Subcategoria;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    // Buscar por nombre exacto
    Optional<Subcategoria> findByNombre(String nombre);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<Subcategoria> findByNombreIgnoreCase(String nombre);

    // Buscar subcategorías por categoría
    List<Subcategoria> findByCategoriaIdAndActivoTrueOrderByOrdenVisualizacionAscNombreAsc(Long categoriaId);

    // Buscar subcategorías por categoría con paginación
    Page<Subcategoria> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);

    // Buscar todas las subcategorías activas
    List<Subcategoria> findByActivoTrueOrderByOrdenVisualizacionAscNombreAsc();

    // Buscar subcategorías activas con paginación
    Page<Subcategoria> findByActivoTrue(Pageable pageable);

    // Buscar por nombre que contenga el término en una categoría específica
    Page<Subcategoria> findByCategoriaIdAndNombreContainingIgnoreCaseAndActivoTrue(
            Long categoriaId, String nombre, Pageable pageable);

    // Buscar por nombre que contenga el término en todas las categorías
    Page<Subcategoria> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    // Buscar por estado (activo/inactivo) en una categoría específica
    Page<Subcategoria> findByCategoriaIdAndActivo(Long categoriaId, Boolean activo, Pageable pageable);



    // Consulta para buscar subcategorías por nombre o descripción
    @Query("SELECT s FROM Subcategoria s WHERE " +
           "(LOWER(s.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "s.activo = :activo")
    Page<Subcategoria> findBySearchTerm(@Param("searchTerm") String searchTerm, 
                                      @Param("activo") Boolean activo, 
                                      Pageable pageable);

    // Consulta para buscar subcategorías por nombre o descripción en una categoría específica
    @Query("SELECT s FROM Subcategoria s WHERE s.categoria.id = :categoriaId AND " +
           "(LOWER(s.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "s.activo = :activo")
    Page<Subcategoria> findBySearchTermAndCategoriaId(@Param("categoriaId") Long categoriaId,
                                                    @Param("searchTerm") String searchTerm, 
                                                    @Param("activo") Boolean activo, 
                                                    Pageable pageable);



    // Verificar si existe una subcategoría con el mismo nombre en la misma categoría (excluyendo la actual)
    @Query("SELECT COUNT(s) > 0 FROM Subcategoria s WHERE s.categoria.id = :categoriaId AND LOWER(s.nombre) = LOWER(:nombre) AND s.id != :id")
    Boolean existsByNombreIgnoreCaseAndCategoriaIdAndIdNot(
            @Param("nombre") String nombre, 
            @Param("categoriaId") Long categoriaId, 
            @Param("id") Long id);

    // Verificar si existe una subcategoría con el mismo nombre en la misma categoría
    Boolean existsByNombreIgnoreCaseAndCategoriaId(String nombre, Long categoriaId);

    // Obtener el máximo orden de visualización en una categoría
    @Query("SELECT COALESCE(MAX(s.ordenVisualizacion), 0) FROM Subcategoria s WHERE s.categoria.id = :categoriaId")
    Integer findMaxOrdenVisualizacionByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Buscar subcategorías ordenadas por orden de visualización
    List<Subcategoria> findAllByOrderByOrdenVisualizacionAscNombreAsc();

    // Buscar subcategorías de una categoría ordenadas por orden de visualización
    List<Subcategoria> findByCategoriaIdOrderByOrdenVisualizacionAscNombreAsc(Long categoriaId);

    // Contar subcategorías activas por categoría
    Long countByCategoriaIdAndActivoTrue(Long categoriaId);

    // Buscar subcategorías creadas por un usuario específico
    Page<Subcategoria> findByCreadoPor(String creadoPor, Pageable pageable);

    // Buscar por estado (activo/inactivo)
    Page<Subcategoria> findByActivo(Boolean activo, Pageable pageable);

    // Consulta personalizada para filtros específicos
    @Query("SELECT s FROM Subcategoria s WHERE " +
           "(:categoriaId IS NULL OR s.categoria.id = :categoriaId) AND " +
           "(:urgente IS NULL OR s.permiteUrgente = :urgente) AND " +
           "(:conMateriales IS NULL OR s.incluyeMateriales = :conMateriales) AND " +
           "(:boletaFactura IS NULL OR s.emiteBoletaFactura = :boletaFactura) AND " +
           "(:garantia IS NULL OR s.ofreceGarantia = :garantia) AND " +
           "(:interior IS NULL OR s.trabajoInterior = :interior) AND " +
           "(:exterior IS NULL OR s.trabajoExterior = :exterior) AND " +
           "(:altura IS NULL OR s.trabajoEnAltura = :altura) AND " +
           "(:domicilio IS NULL OR s.servicioDomicilio = :domicilio) AND " +
           "(:atencion24h IS NULL OR s.atencion24_7 = :atencion24h) AND " +
           "(:radioMaximo IS NULL OR s.radioCobertura <= :radioMaximo) AND " +
           "(:tipoPrecio IS NULL OR s.tipoPrecio = :tipoPrecio) AND " +
           "(:ratingMinimo IS NULL OR s.ratingMinimo >= :ratingMinimo) AND " +
           "s.activo = true " +
           "ORDER BY s.ordenVisualizacion ASC, s.nombre ASC")
    Page<Subcategoria> findSubcategoriasConFiltros(
            @Param("categoriaId") Long categoriaId,
            @Param("urgente") Boolean urgente,
            @Param("conMateriales") Boolean conMateriales,
            @Param("boletaFactura") Boolean boletaFactura,
            @Param("garantia") Boolean garantia,
            @Param("interior") Boolean interior,
            @Param("exterior") Boolean exterior,
            @Param("altura") Boolean altura,
            @Param("domicilio") Boolean domicilio,
            @Param("atencion24h") Boolean atencion24h,
            @Param("radioMaximo") Integer radioMaximo,
            @Param("tipoPrecio") String tipoPrecio,
            @Param("ratingMinimo") Double ratingMinimo,
            Pageable pageable);
}