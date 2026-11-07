package com.raposo.gerenciador_autores_obras.api.mapper;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.ObraEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ObraMapper {

    ObraEntity ParaDTO(ObraEntity entity);

    ObraEntity ParaEntity(ObraMapper dto);

}
