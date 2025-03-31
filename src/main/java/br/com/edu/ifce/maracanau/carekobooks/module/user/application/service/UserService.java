package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSocialSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.service.enums.ToggleAction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public ApplicationPage<UserDTO> search(UserSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toDTO));
    }

    public ApplicationPage<UserDTO> search(UserSocialSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toDTO));
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDTO);
    }

    @Transactional
    public void update(String username, UserRegisterRequest request) throws Exception {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to update this user");
        }

        userMapper.updateEntity(user, request);
        userValidator.validate(user);
        userRepository.save(user);
    }

    @Transactional
    public void updateFollowingByUsername(String username, String targetUsername, ToggleAction action) {
        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to perform this action");
        }

        if (username.equals(targetUsername)) {
            throw new BadRequestException("You cannot follow/unfollow yourself");
        }

        var userMap = userRepository
                .findByUsernameIn(List.of(username, targetUsername))
                .stream()
                .collect(Collectors.toMap(User::getUsername, user -> user));

        var user = userMap.get(username);
        var target = userMap.get(targetUsername);
        if (user == null || target == null) {
            throw new NotFoundException("One or both users not found");
        }

        var isFollowingRequested = action == ToggleAction.ASSIGN;
        var isUserFollowing = user.getFollowing().contains(target);
        if (isFollowingRequested == isUserFollowing) {
            throw new BadRequestException(isFollowingRequested
                    ? "User is already following the target"
                    : "User does not follow the target"
            );
        }

        if (isFollowingRequested) user.getFollowing().add(target);
        else user.getFollowing().remove(target);
        userRepository.save(user);
    }

    @Transactional
    public void updateImageByUsername(String username, MultipartFile image) throws Exception {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to update the image of this user");
        }

        user.setImage(imageMapper.toModel(imageService.create(image)));
        userRepository.save(user);
    }

    @Transactional
    public void deleteImageByUsername(String username) throws Exception {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to update the image of this user");
        }

        if (user.getImage() == null) {
            throw new NotFoundException("Image not found or already deleted");
        }

        imageService.deleteById(user.getImage().getId());
        user.setImage(null);
        userRepository.save(user);
    }

    @Transactional
    public void deleteByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException("User not found");
        }

        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to delete this user");
        }

        userRepository.deleteByUsername(username);
    }

}
