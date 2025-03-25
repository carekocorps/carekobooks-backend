package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.validator.UserValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.provider.UserContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public ApplicationPage<UserDTO> search(UserSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toDTO));
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDTO);
    }

    @Transactional
    public void update(String username, UserRegisterRequest request) {
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
    public void updateFollowingByUsernameAndTargetUsername(String username, String targetUsername, boolean isFollowing) {
        if (!UserContextProvider.isCurrentUserAuthorized(username)) {
            throw new ForbiddenException("You are not allowed to follow this user");
        }

        if (username.equals(targetUsername)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var target = userRepository
                .findByUsername(targetUsername)
                .orElseThrow(() -> new NotFoundException("Target not found"));

        var userContainsTarget = user.getFollowing().contains(target);
        if (isFollowing && !userContainsTarget) user.getFollowing().add(target);
        else if (!isFollowing && userContainsTarget) user.getFollowing().remove(target);
        else throw new BadRequestException("Operation has already been performed");
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
