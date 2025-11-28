package com.ays.kardex.repository;

import com.ays.kardex.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByRuc(String ruc);

    boolean existsByRucAndIdNot(String ruc, Long id);

    @Query("SELECT c FROM Company c WHERE (:name IS NULL OR :name = '' OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:taxId IS NULL OR :taxId = '' OR LOWER(c.ruc) LIKE LOWER(CONCAT('%', :taxId, '%')))")
    Page<Company> buscarPorNombreORuc(@Param("name") String name, @Param("taxId") String taxId, Pageable pageable);
}
