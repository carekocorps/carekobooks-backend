package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = ImageMapper.class
)
public interface UserMapper {

    @IgnoreBaseModelFields
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "otpValidationType", ignore = true)
    @Mapping(target = "otpExpiresAt", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "threads", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toModel(UserRegisterRequest request);
    UserResponse toResponse(User user);

    @IgnoreBaseModelFields
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "otp", ignore = true)
    @Mapping(target = "otpValidationType", ignore = true)
    @Mapping(target = "otpExpiresAt", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "threads", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateModel(@MappingTarget User user, UserUpdateRequest request);

}
