package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseEntity;

public interface BaseValidator<T extends BaseEntity> {

    void validate(T model);

}
