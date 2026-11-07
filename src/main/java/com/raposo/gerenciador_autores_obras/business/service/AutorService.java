package com.raposo.gerenciador_autores_obras.business.service;
import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.api.mapper.AutorMapper;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    public AutorResponseDTO salvarAutor(AutorRequestDTO dto){
        AutorEntity entity = autorMapper.paraEntity(dto);

        return autorMapper.paraResponse(
                autorRepository.save(entity)
        );
    }

    private void validar

}
