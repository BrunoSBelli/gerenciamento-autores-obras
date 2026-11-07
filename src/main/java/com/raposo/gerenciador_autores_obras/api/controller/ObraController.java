package com.raposo.gerenciador_autores_obras.api.controller;


import com.raposo.gerenciador_autores_obras.api.dto.Request.ObraRequestDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
