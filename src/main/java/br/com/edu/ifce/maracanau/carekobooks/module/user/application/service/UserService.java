package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSocialQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.IntentType;
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

    public ApplicationPage<UserResponse> search(UserQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toResponse));
    }

    public ApplicationPage<UserResponse> search(UserSocialQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toResponse));
    }

    public Optional<UserResponse> find(String username) {
        return userRepository.findByUsername(username).map(userMapper::toResponse);
    }

    public List<UserResponse> findAllFollowers(String username) {
        var query = new UserSocialQuery();
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWER);

        var specification = query.getSpecification();
        return userRepository.findAll(specification).stream().map(userMapper::toResponse).toList();
    }

    @Transactional
    public UserResponse update(String username, UserUpdateRequest request, MultipartFile image) throws Exception {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (UserContextProvider.isUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to update this user");
        }

        if (image != null) {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userMapper.updateModel(user, request);
        userValidator.validate(user);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void changeFollowing(String username, String targetUsername, IntentType action) {
        if (UserContextProvider.isUserUnauthorized(username)) {
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

        var isFollowingRequested = action == IntentType.ASSIGN;
        var isUserFollowing = user.getFollowing().contains(target);
        if (isFollowingRequested == isUserFollowing) {
            throw new BadRequestException(isFollowingRequested
                    ? "User is already following the target"
                    : "User does not follow the target"
            );
        }

        if (isFollowingRequested) userRepository.follow(user.getId(), target.getId());
        else userRepository.unfollow(user.getId(), target.getId());
    }

    @Transactional
    public void changeImage(String username, MultipartFile image) throws Exception {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (UserContextProvider.isUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to update the image of this user");
        }

        if (image == null && user.getImage() != null) {
            imageService.delete(user.getImage().getId());
            user.setImage(null);
        } else {
            user.setImage(imageMapper.toModel(imageService.create(image)));
        }

        userRepository.save(user);
    }

    @Transactional
    public void delete(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException("User not found");
        }

        if (UserContextProvider.isUserUnauthorized(username)) {
            throw new ForbiddenException("You are not allowed to delete this user");
        }

        userRepository.deleteByUsername(username);
    }

}
