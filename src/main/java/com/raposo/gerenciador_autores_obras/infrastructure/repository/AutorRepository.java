package com.raposo.gerenciador_autores_obras.infrastructure.repository;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {

    Optional<AutorEntity> findById(Long Id);
}
