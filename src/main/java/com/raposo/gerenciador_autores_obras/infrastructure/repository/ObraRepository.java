package com.raposo.gerenciador_autores_obras.infrastructure.repository;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObraRepository extends JpaRepository<ObraEntity, Long> {

    List<ObraEntity> findByNomeContainingIgnoreCase(String nome);

    Optional<ObraEntity> findById(Long id);

}
