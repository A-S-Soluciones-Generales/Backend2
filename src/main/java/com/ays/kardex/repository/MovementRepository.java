package com.ays.kardex.repository;

import com.ays.kardex.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByProductoIdOrderByFechaCreacionDesc(Long productoId);

    List<Movement> findByProductoIdAndSedeIdOrderByFechaCreacionDesc(Long productoId, Long sedeId);
}
