package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseUpdateMapper<T extends BaseModel, TRequest extends BaseRequest> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateModel(@MappingTarget T model, TRequest request);

}
