package com.raposo.gerenciador_autores_obras.api.dto.Response;

import com.raposo.gerenciador_autores_obras.api.dto.AutorResumoDTO;

import java.time.LocalDate;
import java.util.Set;

public record ObraResponseDTO(
        Long id, String nome, String descricao,
        LocalDate dataPublicacao, LocalDate dataExposicao,
        Set<AutorResumoDTO> autores
) {}
