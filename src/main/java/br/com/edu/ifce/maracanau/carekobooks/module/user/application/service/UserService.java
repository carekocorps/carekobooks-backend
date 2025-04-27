package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
    public UserResponse update(String username, UserUpdateRequest request, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User not verified");
        }

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to update this user");
        }

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userMapper.updateModel(user, request);
        userValidator.validate(user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changeImage(String username, MultipartFile image) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User not verified");
        }

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to update the image of this user");
        }

        user.setImage(Optional
                .ofNullable(image)
                .map(file -> imageMapper.toModel(imageService.create(file)))
                .orElseGet(() -> {
                    if (user.getImage() == null) {
                        throw new NotFoundException("No image found or already deleted");
                    }

                    imageService.delete(user.getImage().getId());
                    return null;
                })
        );

        userRepository.save(user);
    }

    @Transactional
    public void delete(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User not verified");
        }

        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to delete this user");
        }

        userRepository.deleteByUsername(username);
    }

}
