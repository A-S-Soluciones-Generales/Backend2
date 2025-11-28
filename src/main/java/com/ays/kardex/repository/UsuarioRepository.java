package com.ays.kardex.repository;

import com.ays.kardex.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsByRole(Usuario.Role role);

    List<Usuario> findBySedeIdAndActivoTrue(Long sedeId);
}
