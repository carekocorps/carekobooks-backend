package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.exception.user.UserNotVerifiedException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.annotation.AuthenticatedUserMatchRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public ApplicationPage<UserResponse> search(UserQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toResponse));
    }

    public Optional<UserResponse> find(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponse);
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "username", exception = UserModificationForbiddenException.class)
    public UserResponse update(String username, UserUpdateRequest request, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userMapper.updateModel(user, request);
        userValidator.validate(user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "username", exception = UserModificationForbiddenException.class)
    public void changeImage(String username, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        user.setImage(Optional
                .ofNullable(image)
                .map(file -> imageMapper.toModel(imageService.create(file)))
                .orElseGet(() -> {
                    if (user.getImage() == null) {
                        throw new ImageNotFoundException();
                    }

                    imageService.delete(user.getImage().getId());
                    return null;
                })
        );

        userRepository.save(user);
    }

    @Transactional
    @AuthenticatedUserMatchRequired(target = "username", exception = UserModificationForbiddenException.class)
    public void delete(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException();
        }

        userRepository.deleteByUsername(username);
    }

}
