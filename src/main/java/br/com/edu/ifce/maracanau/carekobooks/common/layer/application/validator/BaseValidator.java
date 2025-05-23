package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;

public interface BaseValidator<T extends BaseModel> {

    void validate(T model);

}
