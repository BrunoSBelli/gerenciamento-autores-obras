package com.raposo.gerenciador_autores_obras.api.mapper;

import com.raposo.gerenciador_autores_obras.api.dto.ObraResumoDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Request.AutorRequestDTO;
import com.raposo.gerenciador_autores_obras.api.dto.Response.AutorResponseDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AutorMapper {

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "obras", ignore = true)   // o vínculo é feito no Service
        AutorEntity paraEntity(AutorRequestDTO dto);

        AutorResponseDTO paraResponse(AutorEntity entity);

        ObraResumoDTO paraResumo(ObraEntity obra);  // o MapStruct reaproveita para o Set
}
