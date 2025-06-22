package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserModificationForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Transactional(readOnly = true)
    public ApplicationPage<SimplifiedUserResponse> search(UserQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toSimplifiedResponse));
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> find(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponse);
    }

    @Transactional
    public UserResponse signUp(UserSignUpRequest request, MultipartFile image) {
        var keycloakId = UUID.fromString(keycloakService.signUp(request).getId());
        try {
            var user = userMapper.toEntity(keycloakId, request);
            if (image != null) {
                user.setImage(imageMapper.toEntity(imageService.create(image)));
            }

            return userMapper.toResponse(userRepository.save(user));
        } catch (Exception e) {
            keycloakService.delete(keycloakId);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public void resetVerificationEmail(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        keycloakService.resetVerificationEmail(user.getKeycloakId());
    }

    @Transactional(readOnly = true)
    public void changeEmail(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(user.getKeycloakId(), UserModificationForbiddenException.class);
        keycloakService.changeEmail(user.getKeycloakId());
    }

    @Transactional
    public void changeImage(String username, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(user.getKeycloakId(), UserModificationForbiddenException.class);
        if (image == null && user.getImage() == null) {
            throw new ImageNotFoundException();
        }

        if (user.getImage() != null) {
            imageService.delete(user.getImage().getId());
        }

        user.setImage(Optional
                .ofNullable(image)
                .map(file -> imageMapper.toEntity(imageService.create(file)))
                .orElse(null)
        );

        userRepository.save(user);
    }

    @Transactional
    public void update(String username, UserUpdateRequest request, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(user.getKeycloakId(), UserModificationForbiddenException.class);
        if (user.getImage() != null) {
            imageService.delete(user.getImage().getId());
        }

        if (image != null) {
            user.setImage(imageMapper.toEntity(imageService.create(image)));
        }

        userMapper.updateEntity(user, request);
        userRepository.save(user);
        keycloakService.update(user.getKeycloakId(), request);
    }

    @Transactional
    public void delete(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        KeycloakContextProvider.assertAuthorized(user.getKeycloakId(), UserModificationForbiddenException.class);
        userRepository.delete(user);
        keycloakService.delete(user.getKeycloakId());
    }

}
