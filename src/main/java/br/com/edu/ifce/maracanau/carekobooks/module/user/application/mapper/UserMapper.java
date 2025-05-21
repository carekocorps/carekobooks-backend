package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = ImageMapper.class
)
public abstract class UserMapper {

    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;

    @IgnoreBaseModelFields
    @Mapping(target = "tempEmail", ignore = true)
    @Mapping(target = "tempPassword", ignore = true)
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
    public abstract User toModel(UserSignUpRequest request);
    public abstract UserResponse toResponse(User user);

    @IgnoreBaseModelFields
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "tempEmail", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tempPassword", ignore = true)
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
    public abstract void updateModel(@MappingTarget User user, UserUpdateRequest request);

    public User toModel(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
