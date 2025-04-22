package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterInitializationRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        config = BaseUpdateMapper.class,
        uses = ImageMapper.class
)
public interface UserMapper extends BaseUpdateMapper<User, UserUpdateRequest> {

    User toModel(UserRegisterInitializationRequest request);
    UserResponse toResponse(User user);

}
