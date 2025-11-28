package com.ays.kardex.repository;

import com.ays.kardex.entity.Sede;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {

    boolean existsByIdAndCompanyId(Long sedeId, Long companyId);

    List<Sede> findByCompanyId(Long companyId);
}
