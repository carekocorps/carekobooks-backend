package br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.shared.infra.model.BaseModel;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseModel, TRequestDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget T entity, TRequestDTO entityRequestDTO);

}
