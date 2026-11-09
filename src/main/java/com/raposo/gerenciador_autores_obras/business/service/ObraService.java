package com.raposo.gerenciador_autores_obras.business.service;


import com.raposo.gerenciador_autores_obras.api.dto.Request.ObraRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.ObraResponseDTO;
import com.raposo.gerenciador_autores_obras.api.mapper.ObraMapper;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.BusinessException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ResourceNotFoundException;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.ObraRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ObraService {


    private final ObraRepository obraRepository;
    private final AutorRepository autorRepository;
    private final ObraMapper obraMapper;


    @Transactional
    public ObraResponseDTO salvarObra(ObraRequestDTO dto){

        validarRegrasDeNegocio(dto);

        Set<AutorEntity> autores = buscarAutores(dto.autoresIds());

        //Salvando antes para já ter ID
        ObraEntity entity = obraRepository.save(obraMapper.paraEntity(dto));

        //Vincula os dois lados (grava na tabela autor_obra ao fechar a transação)
        autores.forEach(autor -> autor.adicionarObra(entity));

        return obraMapper.paraResponse(entity);
    }

    public List<ObraResponseDTO> buscarObraPorNome(String nome){
        return obraRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(obraMapper::paraResponse)   // converte cada obra
                .toList();
    }

    public ObraResponseDTO buscarObraPorId(Long id){
        ObraEntity entity = obraRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id não cadastrado!" + id));

        return obraMapper.paraResponse(entity);
    }

    @Transactional
    public void deletarObraPorId(Long id) {
        ObraEntity obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obra não encontrada com o id: " + id));

        // Desfaz o vínculo pelo lado DONO antes de apagar.
        // A cópia (new HashSet<>) evita erro ao alterar a lista enquanto ela é percorrida.
        new HashSet<>(obra.getAutores()).forEach(autor -> autor.removerObra(obra));

        obraRepository.delete(obra);
    }

    @Transactional
    public ObraResponseDTO atualizarObra(Long id, ObraRequestDTO dto) {

        ObraEntity obra = obraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada com o id: " + id));

        validarRegrasDeNegocio(dto);                                   // a regra das datas, que já existe
        Set<AutorEntity> novosAutores = buscarAutores(dto.autoresIds()); // o metodo que já existe

        obraMapper.atualizarEntity(dto, obra);
        sincronizarAutores(obra, novosAutores);

        return obraMapper.paraResponse(obra);
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

            throw new ResourceNotFoundException("Autor(es) não encontrado(s): " + idsFaltando);
        }

        return new HashSet<>(autoresEncontrados);
    }

    private void sincronizarAutores(ObraEntity obra, Set<AutorEntity> novosAutores) {

        Set<Long> idsNovos = novosAutores.stream()
                .map(AutorEntity::getId)
                .collect(Collectors.toSet());

        // Quem saiu da lista: desfaz o vínculo (pelo lado DONO)
        // A cópia (new HashSet) evita alterar a coleção enquanto ela é percorrida
        new HashSet<>(obra.getAutores()).stream()
                .filter(autor -> !idsNovos.contains(autor.getId()))
                .forEach(autor -> autor.removerObra(obra));

        // Quem entrou: cria o vínculo (quem já estava é ignorado, porque é um Set)
        novosAutores.forEach(autor -> autor.adicionarObra(obra));
    }

}
