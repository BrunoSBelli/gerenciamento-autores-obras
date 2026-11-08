package com.raposo.gerenciador_autores_obras.api.mapper;

import com.raposo.gerenciador_autores_obras.api.dto.AutorResumoDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Request.ObraRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.ObraResponseDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ObraMapper {

    ObraResponseDTO paraResponse(ObraEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)
    ObraEntity paraEntity(ObraRequestDTO dto);

    // O MapStruct usa este metodo sozinho para converter cada autor do Set
    AutorResumoDTO paraResumo(AutorEntity autor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autores", ignore = true)   // sincronizados no Service, pelos ids
    void atualizarEntity(ObraRequestDTO dto, @MappingTarget ObraEntity entity);
}