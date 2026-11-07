package com.raposo.gerenciador_autores_obras.api.controller;


import com.raposo.gerenciador_autores_obras.business.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autor")
@RequiredArgsConstructor


@Tag(name="Autores", description = "CRUD")
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @Operation(summary = "Salvar Autores", description = "Cria um novo Autor")
    public ResponseEntity<AutorDTO> salvarAutor(@RequestBody AutorDTO dto){
        return ResponseEntity.ok(autorService.salvarAutor(dto));
    }

}
