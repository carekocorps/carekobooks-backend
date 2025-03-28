package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements BaseUpdateMapper<User, UserRegisterRequest> {

    public abstract User toModel(UserRegisterRequest request);
    public abstract UserDTO toDTO(User user);

}
