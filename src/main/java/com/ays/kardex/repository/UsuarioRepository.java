package com.ays.kardex.repository;

import com.ays.kardex.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsByRole(Usuario.Role role);

    List<Usuario> findBySedeIdAndActivoTrue(Long sedeId);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Usuario> findByCompanyIdAndActivoTrue(Long companyId);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) "
            + "OR LOWER(u.documento) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Usuario> buscarPorTermino(@Param("search") String search);
}
