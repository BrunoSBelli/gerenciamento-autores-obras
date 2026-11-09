package com.raposo.gerenciador_autores_obras.api.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record AutorRequestDTO(
        @NotBlank String nome,
        String sexo,
        @NotBlank @Email String email,
        @Past LocalDate dataNascimento,    // data no passado; null é aceito
        @NotBlank String paisOrigem,
        @CPF String cpf                    // valida os dígitos; null é aceito
) {}
