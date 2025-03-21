package br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseModel, TRequest extends BaseRequest> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget T model, TRequest request);

}
