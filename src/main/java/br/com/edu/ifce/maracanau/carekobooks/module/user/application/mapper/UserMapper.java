package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterInitializationRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", config = BaseUpdateMapper.class)
public abstract class UserMapper implements BaseUpdateMapper<User, UserUpdateRequest> {

    @Autowired
    protected ImageMapper imageMapper;

    public abstract User toModel(UserRegisterInitializationRequest request);

    @Mapping(target = "image", expression = "java(imageMapper.toResponse(user.getImage()))")
    public abstract UserResponse toResponse(User user);

}
