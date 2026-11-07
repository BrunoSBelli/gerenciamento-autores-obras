package com.raposo.gerenciador_autores_obras.infrastructure.record;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErroResponseDTO(
        LocalDateTime timestamp,
        String message,
        int status,
        String path
) {}
