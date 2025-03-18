package br.com.edu.ifce.maracanau.carekobooks.mapper;

import br.com.edu.ifce.maracanau.carekobooks.model.BaseEntity;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, TRequestDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget T entity, TRequestDTO entityRequestDTO);

}
