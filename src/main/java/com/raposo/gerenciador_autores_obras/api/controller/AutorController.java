package com.raposo.gerenciador_autores_obras.api.controller;


import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.business.service.AutorService;
import com.raposo.gerenciador_autores_obras.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autor")
@RequiredArgsConstructor


@Tag(name="Autores", description = "CRUD")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @Operation(summary = "Salvar Autor", description = "Cria um novo Autor")
    public ResponseEntity<AutorResponseDTO> salvarAutor(@Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.salvarAutor(dto));
    }

    @GetMapping
    @Operation(summary = "Buscar Autor", description = "Retorna autor por email")
    public ResponseEntity<AutorResponseDTO> buscarObraPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(autorService.buscarAutorPorEmail(email));
    }

    @DeleteMapping
    @Operation(summary = "Deletar Autor", description = "Remove um autor pelo e-mail")
    public ResponseEntity<Void> deletarAutor(@RequestParam @NotBlank @Email String email) {
        autorService.deletarAutorPorEmail(email);
        return ResponseEntity.noContent().build();   // 204: deu certo, nada a devolver
    }


}
