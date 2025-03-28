package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserContextMapper {

    public User toModel() {
        return (User) UserContextProvider.getUserDetails();
    }

}
