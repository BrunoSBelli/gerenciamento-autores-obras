package com.raposo.gerenciador_autores_obras.api.controller;


import com.raposo.gerenciador_autores_obras.api.dto.Request.ObraRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.ObraResponseDTO;
import com.raposo.gerenciador_autores_obras.business.service.ObraService;
import com.raposo.gerenciador_autores_obras.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/obra")
@RequiredArgsConstructor

@Tag(name="Obras", description = "CRUD")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class ObraController {

    private final ObraService obraService;

    @PostMapping
    @Operation(summary = "Salvar Obras", description = "Cria uma nova obra")
    public ResponseEntity<ObraResponseDTO> salvarObra(@Valid @RequestBody ObraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(obraService.salvarObra(dto));
    }

    @GetMapping
    @Operation(summary = "Buscar Obra", description = "Retorna obra por nome")
    public ResponseEntity<List<ObraResponseDTO>> buscarObraPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(obraService.buscarObraPorNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObraResponseDTO> buscarObraPorId(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.buscarObraPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Obra", description = "Remove uma obra pelo id")
    public ResponseEntity<Void> deletarObra(@PathVariable Long id) {
        obraService.deletarObraPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Obra", description = "Substitui os dados de uma obra")
    public ResponseEntity<ObraResponseDTO> atualizarObra(@PathVariable Long id,
                                                         @Valid @RequestBody ObraRequestDTO dto) {
        return ResponseEntity.ok(obraService.atualizarObra(id, dto));
    }
}
