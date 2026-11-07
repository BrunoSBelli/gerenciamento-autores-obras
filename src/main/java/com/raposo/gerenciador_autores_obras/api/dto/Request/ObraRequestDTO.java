package com.raposo.gerenciador_autores_obras.api.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record ObraRequestDTO(
        @NotBlank String nome,
        @Size(max = 240) String descricao,
        LocalDate dataPublicacao,
        LocalDate dataExposicao,
        Set<Long> autoresIds               // referencia autores JÁ cadastrados pelo ID
) {}
