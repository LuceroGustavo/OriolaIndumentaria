package com.otz.repo;

import com.otz.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Buscar productos por categoría
    List<Product> findByCategoria(String categoria);
    
    // Buscar productos activos
    List<Product> findByActivoTrue();
    
    // Buscar productos destacados y activos
    List<Product> findByEsDestacadoTrueAndActivoTrue();
    
    // Buscar productos nuevos y activos
    List<Product> findByEsNuevoTrueAndActivoTrue();
    
    // Buscar productos por género
    List<Product> findByGeneroAndActivoTrue(String genero);
    
    // Buscar productos por temporada
    List<Product> findByTemporadaAndActivoTrue(String temporada);
    
    // Buscar productos con descuento
    @Query("SELECT p FROM Product p WHERE p.descuentoPorcentaje > 0 AND p.activo = true")
    List<Product> findProductosConDescuento();
    
    // Buscar productos por rango de precio
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :precioMin AND :precioMax AND p.activo = true")
    List<Product> findByPrecioBetween(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
    
    // Buscar productos por nombre (búsqueda parcial)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = true")
    List<Product> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar productos por color
    @Query("SELECT p FROM Product p WHERE LOWER(p.color) LIKE LOWER(CONCAT('%', :color, '%')) AND p.activo = true")
    List<Product> findByColorContainingIgnoreCase(@Param("color") String color);
    
    // Contar productos por categoría
    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoria = :categoria AND p.activo = true")
    Long countByCategoria(@Param("categoria") String categoria);
    
    // Obtener productos más recientes
    @Query("SELECT p FROM Product p WHERE p.activo = true ORDER BY p.fechaCreacion DESC")
    List<Product> findProductosRecientes();
}
