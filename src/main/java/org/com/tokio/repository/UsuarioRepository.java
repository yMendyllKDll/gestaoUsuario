package org.com.tokio.repository;

import org.com.tokio.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Usuario> findAllByOrderByNomeAsc(Pageable pageable);
    Page<Usuario> findAllByOrderByDataCriacaoDesc(Pageable pageable);
} 