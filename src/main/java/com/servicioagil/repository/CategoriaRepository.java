package com.servicioagil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicioagil.entity.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    // Buscar solo categorías activas
    List<Categoria> findByActivoTrueOrderByOrdenVisualizacionAscNombreAsc();

    // Buscar categorías activas con paginación
    Page<Categoria> findByActivoTrue(Pageable pageable);

    // Buscar por nombre que contenga el término (búsqueda parcial)
    Page<Categoria> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    // Buscar por estado (activo/inactivo)
    Page<Categoria> findByActivo(Boolean activo, Pageable pageable);

    // Consulta personalizada para obtener categorías con contador de subcategorías
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.subcategorias s WHERE c.activo = :activo AND (s.activo = true OR s.activo IS NULL) ORDER BY c.ordenVisualizacion ASC, c.nombre ASC")
    List<Categoria> findCategoriasWithActiveSubcategorias(@Param("activo") Boolean activo);

    // Consulta para buscar categorías por nombre o descripción
    @Query("SELECT c FROM Categoria c WHERE " +
           "(LOWER(c.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "c.activo = :activo")
    Page<Categoria> findBySearchTerm(@Param("searchTerm") String searchTerm, 
                                   @Param("activo") Boolean activo, 
                                   Pageable pageable);

    // Contar subcategorías activas por categoría
    @Query("SELECT COUNT(s) FROM Subcategoria s WHERE s.categoria.id = :categoriaId AND s.activo = true")
    Long countActiveSubcategoriasByCategoriaId(@Param("categoriaId") Long categoriaId);

    // Verificar si existe una categoría con el mismo nombre (excluyendo la actual en updates)
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.id != :id")
    Boolean existsByNombreIgnoreCaseAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // Verificar si existe una categoría con el mismo nombre
    Boolean existsByNombreIgnoreCase(String nombre);

    // Obtener el máximo orden de visualización
    @Query("SELECT COALESCE(MAX(c.ordenVisualizacion), 0) FROM Categoria c")
    Integer findMaxOrdenVisualizacion();

    // Buscar categorías ordenadas por orden de visualización
    List<Categoria> findAllByOrderByOrdenVisualizacionAscNombreAsc();

    // Buscar categorías creadas por un usuario específico
    Page<Categoria> findByCreadoPor(String creadoPor, Pageable pageable);
}