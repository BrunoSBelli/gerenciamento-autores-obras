package com.raposo.gerenciador_autores_obras.business.service;
import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.api.mapper.AutorMapper;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.BusinessException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ConflictException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ResourceNotFoundException;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    @Transactional
    public AutorResponseDTO salvarAutor(AutorRequestDTO dto){

        validarCpfObrigatorioParaBrasileiros(dto);

        AutorEntity entity = autorMapper.paraEntity(dto);

        return autorMapper.paraResponse(
                autorRepository.save(entity)
        );
    }

    public AutorResponseDTO buscarAutorPorEmail(String email){
        AutorEntity entity = autorRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Autor não encontrado com o e-mail: " + email));
        return autorMapper.paraResponse(entity);
    }

    @Transactional
    public void deletarAutorPorEmail(String email) {
        AutorEntity autor = autorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o e-mail: " + email));

        // Autor é o lado DONO: o Hibernate limpa as linhas dele em autor_obra sozinho.
        // As obras NÃO são apagadas (não temos cascade), só o vínculo some.
        autorRepository.delete(autor);
    }

    @Transactional
    public AutorResponseDTO atualizarAutor(Long id, AutorRequestDTO dto) {

        AutorEntity autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado com o id: " + id));

        //Valida ANTES de alterar a entidade
        validarCpfObrigatorioParaBrasileiros(dto);

        if (autorRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new ConflictException("E-mail já cadastrado por outro autor");
        }
        if (temCpf(dto) && autorRepository.existsByCpfAndIdNot(dto.cpf(), id)) {
            throw new ConflictException("CPF já cadastrado por outro autor");
        }

        //Copia os campos para a entidade existente
        autorMapper.atualizarEntity(dto, autor);

        //Sem save(): o dirty checking grava ao fechar a transação
        return autorMapper.paraResponse(autor);
    }




    private void validarCpfObrigatorioParaBrasileiros(AutorRequestDTO dto) {
        if (ehBrasileiro(dto.paisOrigem()) && !temCpf(dto)) {
            throw new BusinessException("CPF é obrigatório para autores Brasileiros");
        }
    }


    private boolean ehBrasileiro(String paisOrigem){
        return "Brasil".equalsIgnoreCase(paisOrigem);
    }

    private boolean temCpf(AutorRequestDTO dto) { return dto.cpf() != null && !dto.cpf().isBlank(); }
}
