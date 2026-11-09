package com.raposo.gerenciador_autores_obras.api.controller;

import com.raposo.gerenciador_autores_obras.business.service.AuthService;
import com.raposo.gerenciador_autores_obras.infrastructure.record.TokenResponseDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.record.UsuarioRequestDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.record.UsuarioResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.cadastrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
