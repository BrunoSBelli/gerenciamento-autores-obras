package com.raposo.gerenciador_autores_obras.infrastructure.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponseDTO(
        LocalDateTime timestamp,
        String message,
        int status,
        String path,
        Map<String, String> erros      // só preenchido nos erros de validação
) {}
