package br.com.edu.ifce.maracanau.carekobooks.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.UserSocialQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.AuthenticatedUserProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserSocialService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ApplicationPage<UserResponse> search(UserSocialQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(userRepository.findAll(specification, pageRequest).map(userMapper::toResponse));
    }

    public List<UserResponse> findFollowers(String username) {
        var query = new UserSocialQuery();
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWER);

        var specification = query.getSpecification();
        return userRepository.findAll(specification).stream().map(userMapper::toResponse).toList();
    }

    @Transactional
    public void changeFollowing(String username, String targetUsername, boolean isFollowingRequested) {
        if (AuthenticatedUserProvider.isAuthenticatedUserUnauthorized(username)) {
            throw new UserModificationForbiddenException();
        }

        if (username.equals(targetUsername)) {
            throw new UserSelfFollowingException();
        }

        var users = userRepository
                .findByUsernameIn(List.of(username, targetUsername))
                .stream()
                .collect(Collectors.toMap(User::getUsername, user -> user));

        var user = users.get(username);
        var target = users.get(targetUsername);
        if (user == null || target == null) {
            throw new UserNotFoundException("One or both users were not found");
        }

        if (!user.isEnabled() || !target.isEnabled()) {
            throw new UserNotVerifiedException("One or both users are not verified");
        }

        var isUserFollowing = user.getFollowing().contains(target);
        if (isUserFollowing == isFollowingRequested) {
            throw isFollowingRequested
                    ? new UserAlreadyVerifiedException()
                    : new UserNotFollowingException();
        }

        if (isFollowingRequested) userRepository.follow(user.getId(), target.getId());
        else userRepository.unfollow(user.getId(), target.getId());
    }

}
