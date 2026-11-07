package com.raposo.gerenciador_autores_obras.infrastructure.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String senha
) {}
