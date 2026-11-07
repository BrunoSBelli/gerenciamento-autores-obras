package com.raposo.gerenciador_autores_obras.business.service;


import com.raposo.gerenciador_autores_obras.api.dto.Request.ObraRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.ObraResponseDTO;
import com.raposo.gerenciador_autores_obras.api.mapper.ObraMapper;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.BusinessException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.NotFoundException;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.ObraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ObraService {


    private final ObraRepository obraRepository;
    private final AutorRepository autorRepository;
    private final ObraMapper obraMapper;


    public ObraResponseDTO salvarObra(ObraRequestDTO dto){

        validarRegrasDeNegocio(dto);


        Set<AutorEntity> autores = buscarAutores(dto.autoresIds());

        //Salvando antes para já ter ID
        ObraEntity entity = obraRepository.save(obraMapper.paraEntity(dto));

        //Vincula os dois lados (grava na tabela autor_obra ao fechar a transação)
        autores.forEach(autor -> autor.adicionarObra(entity));

        return obraMapper.paraResponse(entity);
    }

    private void validarRegrasDeNegocio(ObraRequestDTO dto){

        //Regra: Obrigtorio pelo menos uma data
        if(dto.dataExposicao()==null && dto.dataPublicacao()==null){
            throw new BusinessException("Informe a data de publicação ou a data de exposição");
        }

    }

    private Set<AutorEntity> buscarAutores(Set<Long> ids){

        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        //buscando autores já cadastrados sistema
        List<AutorEntity> autoresEncontrados = autorRepository.findAllById(ids);

        // Se vieram menos autores do que IDs pedidos, algum não existe
        if (autoresEncontrados.size() != ids.size()) {
            Set<Long> idsEncontrados = autoresEncontrados.stream()
                    .map(AutorEntity::getId)
                    .collect(Collectors.toSet());

            Set<Long> idsFaltando = new HashSet<>(ids);
            idsFaltando.removeAll(idsEncontrados);

            throw new NotFoundException("Autor(es) não encontrado(s): " + idsFaltando);
        }

        return new HashSet<>(autoresEncontrados);
    }

}
