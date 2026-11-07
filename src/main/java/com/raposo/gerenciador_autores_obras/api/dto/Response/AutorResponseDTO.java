package com.raposo.gerenciador_autores_obras.api.dto.Response;

import com.raposo.gerenciador_autores_obras.api.dto.ObraResumoDTO;
import java.time.LocalDate;
import java.util.Set;

public record AutorResponseDTO(
        Long id, String nome, String sexo,
        String email,
        LocalDate dataNascimento,
        String paisOrigem,
        String cpf,
        Set<ObraResumoDTO> obras
) {}