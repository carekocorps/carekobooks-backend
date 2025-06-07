package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface KeycloakUserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    UserRepresentation toRepresentation(UserSignUpRequest request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username", source = "username")
    UserRepresentation toRepresentation(UserUpdateRequest request);

}
