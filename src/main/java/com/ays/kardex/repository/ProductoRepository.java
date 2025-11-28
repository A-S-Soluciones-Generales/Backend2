package com.ays.kardex.repository;

import com.ays.kardex.entity.Producto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.minStock IS NOT NULL AND p.stock <= p.minStock " +
            "AND (:sedeId IS NULL OR p.sede.id = :sedeId)")
    List<Producto> buscarBajoStockPorSede(@Param("sedeId") Long sedeId);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Producto p WHERE p.sede.id = :sedeId")
    Long calcularStockTotalPorSede(@Param("sedeId") Long sedeId);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND (" +
            "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Producto> buscarPorNombreOSku(@Param("search") String search, Pageable pageable);
}
