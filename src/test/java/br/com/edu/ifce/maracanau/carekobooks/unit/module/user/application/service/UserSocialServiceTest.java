package br.com.edu.ifce.maracanau.carekobooks.unit.module.user.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.context.provider.KeycloakContextProvider;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserSocialService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class UserSocialServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserSocialService userSocialService;

    @Test
    void findFollowers_withExistingUsers_shouldReturnSimplifiedUserResponseList() {
        // Arrange
        var followedUser = UserFactory.validUser();
        var followedUserUsername = followedUser.getUsername();
        var users = IntStream
                .range(0, 10)
                .mapToObj(x -> UserFactory.validUserWithFollowing(followedUser))
                .toList();

        when(userRepository.findAll(ArgumentMatchers.<Specification<User>>any()))
                .thenReturn(users);

        when(userMapper.toSimplifiedResponse(any(User.class)))
                .thenReturn(any(SimplifiedUserResponse.class));

        // Act && Assert
        assertThatCode(() -> userSocialService.findFollowers(followedUserUsername)).doesNotThrowAnyException();
        verify(userRepository, times(1)).findAll(ArgumentMatchers.<Specification<User>>any());
        verify(userMapper, times(users.size())).toSimplifiedResponse(any(User.class));
    }

    @Test
    void changeFollowing_withUserSelfFollowing_shouldThrowUserSelfFollowingException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var username = UserFactory.validUser().getUsername();
            var isFollowingRequest = new Random().nextBoolean();

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(username, username, isFollowingRequest)).isInstanceOf(UserSelfFollowingException.class);
            verify(userRepository, never()).findByUsernameIn(ArgumentMatchers.any());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withInvalidUserAndInvalidTargetUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowedUsername = UserFactory.validUser().getUsername();
            var userFollowingUsername = UserFactory.validUser().getUsername();
            var isFollowingRequest = new Random().nextBoolean();

            when(userRepository.findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername)))
                    .thenReturn(List.of());

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowedUsername, userFollowingUsername, isFollowingRequest)).isInstanceOf(UserNotFoundException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withValidUserAndInvalidTargetUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = UserFactory.validUser().getUsername();
            var isFollowingRequest = new Random().nextBoolean();

            when(userRepository.findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername)))
                    .thenReturn(List.of(userFollowed));

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowedUsername, userFollowingUsername, isFollowingRequest)).isInstanceOf(UserNotFoundException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withInvalidUserAndValidTargetUser_shouldThrowNotFoundException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowing = UserFactory.validUser();
            var userFollowedUsername = UserFactory.validUser().getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var isFollowingRequest = new Random().nextBoolean();

            when(userRepository.findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername)))
                    .thenReturn(List.of(userFollowing));

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowedUsername, userFollowingUsername, isFollowingRequest)).isInstanceOf(UserNotFoundException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowedUsername, userFollowingUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(any(UUID.class), ArgumentMatchers.<Class<RuntimeException>>any()), never());
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withValidUserAndValidTargetUserAndUnauthorizedUser_shouldThrowModificationForbiddenException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowing = UserFactory.validUser();

            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var userFollowingKeycloakId = userFollowing.getKeycloakId();
            var isFollowingRequested = new Random().nextBoolean();

            when(userRepository.findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername)))
                    .thenReturn(List.of(userFollowing, userFollowed));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class))
                    .thenThrow(UserModificationForbiddenException.class);

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowingUsername, userFollowedUsername, isFollowingRequested)).isInstanceOf(UserModificationForbiddenException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withFollowingRequestedAndUserAlreadyFollowing_shouldThrowAlreadyFollowingException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowing = UserFactory.validUserWithFollowing(userFollowed);

            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var userFollowingKeycloakId = userFollowing.getKeycloakId();
            var isFollowingRequested = true;

            when(userRepository.findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername)))
                    .thenReturn(List.of(userFollowed, userFollowing));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowingUsername, userFollowedUsername, isFollowingRequested)).isInstanceOf(UserAlreadyFollowingException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withFollowingNotRequestedAndUserNotFollowing_shouldThrowNotFollowingException() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowing = UserFactory.validUser();

            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var userFollowingKeycloakId = userFollowing.getKeycloakId();
            var isFollowingRequested = false;

            when(userRepository.findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername)))
                    .thenReturn(List.of(userFollowed, userFollowing));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            // Act && Assert
            assertThatThrownBy(() -> userSocialService.changeFollowing(userFollowingUsername, userFollowedUsername, isFollowingRequested)).isInstanceOf(UserNotFollowingException.class);
            verify(userRepository, times(1)).findByUsernameIn(List.of(userFollowingUsername, userFollowedUsername));
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withFollowingRequestedAndValidUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowing = UserFactory.validUser();

            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var userFollowingKeycloakId = userFollowing.getKeycloakId();
            var isFollowingRequested = true;

            when(userRepository.findByUsernameIn(ArgumentMatchers.any()))
                    .thenReturn(List.of(userFollowed, userFollowing));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(userRepository)
                    .follow(userFollowing.getId(), userFollowed.getId());

            // Act && Assert
            assertThatCode(() -> userSocialService.changeFollowing(userFollowingUsername, userFollowedUsername, isFollowingRequested)).doesNotThrowAnyException();
            verify(userRepository, times(1)).findByUsernameIn(ArgumentMatchers.any());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, times(1)).follow(userFollowing.getId(), userFollowed.getId());
            verify(userRepository, never()).unfollow(any(Long.class), any(Long.class));
        }
    }

    @Test
    void changeFollowing_withFollowingNotRequestedAndValidUser_shouldSucceed() {
        try (var mockedStatic = mockStatic(KeycloakContextProvider.class)) {
            // Arrange
            var userFollowed = UserFactory.validUser();
            var userFollowing = UserFactory.validUserWithFollowing(userFollowed);

            var userFollowedUsername = userFollowed.getUsername();
            var userFollowingUsername = userFollowing.getUsername();
            var userFollowingKeycloakId = userFollowing.getKeycloakId();
            var isFollowingRequested = false;

            when(userRepository.findByUsernameIn(ArgumentMatchers.any()))
                    .thenReturn(List.of(userFollowed, userFollowing));

            mockedStatic
                    .when(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class))
                    .thenAnswer(invocation -> null);

            doNothing()
                    .when(userRepository)
                    .unfollow(userFollowing.getId(), userFollowed.getId());

            // Act && Assert
            assertThatCode(() -> userSocialService.changeFollowing(userFollowingUsername, userFollowedUsername, isFollowingRequested)).doesNotThrowAnyException();
            verify(userRepository, times(1)).findByUsernameIn(ArgumentMatchers.any());
            mockedStatic.verify(() -> KeycloakContextProvider.assertAuthorized(userFollowingKeycloakId, UserModificationForbiddenException.class), times(1));
            verify(userRepository, never()).follow(any(Long.class), any(Long.class));
            verify(userRepository, times(1)).unfollow(userFollowing.getId(), userFollowed.getId());
        }
    }

}
