package com.raposo.gerenciador_autores_obras.infrastructure.repository;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObraRepository extends JpaRepository<ObraEntity, Long> {

    Optional<ObraEntity> findById(Long Id);
}
