package com.raposo.gerenciador_autores_obras.api.controller;


import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.business.service.AutorService;
import com.raposo.gerenciador_autores_obras.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autor")
@RequiredArgsConstructor


@Tag(name="Autores", description = "CRUD")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @Operation(summary = "Salvar Autores", description = "Cria um novo Autor")
    public ResponseEntity<AutorResponseDTO> salvarAutor(@Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.salvarAutor(dto));
    }

}
