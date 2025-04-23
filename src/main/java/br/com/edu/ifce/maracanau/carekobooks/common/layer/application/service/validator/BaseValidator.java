package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;

public interface BaseValidator<T extends BaseModel> {

    void validate(T model);

}
