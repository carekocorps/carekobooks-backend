package br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
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

    public abstract UserResponse toResponse(User user);
    public abstract SimplifiedUserResponse toSimplifiedResponse(User user);

    @IgnoreBaseEntityFields
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "threads", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "following", ignore = true)
    @Mapping(target = "followers", ignore = true)
    public abstract void updateEntity(@MappingTarget User user, UserUpdateRequest request);

    public User toEntity(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

}
