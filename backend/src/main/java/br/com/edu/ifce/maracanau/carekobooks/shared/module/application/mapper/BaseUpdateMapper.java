package br.com.edu.ifce.maracanau.carekobooks.shared.module.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.model.BaseModel;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

public interface BaseUpdateMapper<T extends BaseModel, TRequest extends BaseRequest> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget T model, TRequest request);

}
