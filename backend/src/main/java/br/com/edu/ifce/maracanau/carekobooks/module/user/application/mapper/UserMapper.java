package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.request.UserRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected UserRepository userRepository;

    public abstract User toModel(UserRequest request);
    public abstract UserDTO toDTO(User user);

    @Named("toUserModelFromId")
    public User toModel(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
