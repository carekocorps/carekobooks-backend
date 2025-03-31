package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements BaseUpdateMapper<User, UserRegisterRequest> {

    @Autowired
    protected ImageMapper imageMapper;

    public abstract User toModel(UserRegisterRequest request);

    @Mapping(target = "image", expression = "java(imageMapper.toDTO(user.getImage()))")
    public abstract UserDTO toDTO(User user);

}
