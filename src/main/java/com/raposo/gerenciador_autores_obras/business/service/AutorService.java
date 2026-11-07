package com.raposo.gerenciador_autores_obras.business.service;
import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.api.mapper.AutorMapper;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.BusinessException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ConflictException;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    public AutorResponseDTO salvarAutor(AutorRequestDTO dto){

        validarRegrasDeNegocio(dto);

        AutorEntity entity = autorMapper.paraEntity(dto);

        return autorMapper.paraResponse(
                autorRepository.save(entity)
        );
    }

    private void validarRegrasDeNegocio(AutorRequestDTO dto){

        if (autorRepository.existsByEmail(dto.email())){
            throw new ConflictException("E-mail já cadastrado! ");
        }

        boolean cpfJáCadastrado = dto.cpf()!=null && !dto.cpf().isBlank();

        //Regra: CPF obrigatorio para Brasileiros
        if(ehBrasileiro(dto.paisOrigem()) && !cpfJáCadastrado){
            throw new BusinessException("CPF é obrigatório para autores Brasileiros");
        }

        //Regra: CPF único
        if (cpfJáCadastrado && autorRepository.existsByCpf(dto.cpf())) {
            throw new ConflictException("CPF já cadastrado");
        }

    }


    private boolean ehBrasileiro(String paisOrigem){
        return "Brasil".equalsIgnoreCase(paisOrigem);
    }
}
