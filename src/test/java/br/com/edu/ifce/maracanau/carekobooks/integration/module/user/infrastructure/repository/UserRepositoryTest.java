package br.com.edu.ifce.maracanau.carekobooks.integration.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void follow_withValidFollowingIdAndValidFollowedId_shouldSucceed() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullId());

        // Act
        userRepository.follow(userFollowing.getId(), userFollowed.getId());
        entityManager.clear();

        var updatedUserFollowed = userRepository.findById(userFollowed.getId());
        var updatedUserFollowing = userRepository.findById(userFollowing.getId());

        // Assert
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(updatedUserFollowed).isPresent();
        assertThat(updatedUserFollowed.get().getFollowers()).hasSize(1);
        assertThat(updatedUserFollowed.get().getFollowing()).isEmpty();
        assertThat(updatedUserFollowing).isPresent();
        assertThat(updatedUserFollowing.get().getFollowers()).isEmpty();
        assertThat(updatedUserFollowing.get().getFollowing()).hasSize(1);
    }

    @Test
    void unfollow_withValidFollowingIdAndValidFollowedId_shouldSucceed() {
        // Arrange
        var userFollowed = userRepository.save(UserFactory.validUserWithNullId());
        var userFollowing = userRepository.save(UserFactory.validUserWithNullIdAndFollowing(userFollowed));

        // Act
        userRepository.unfollow(userFollowing.getId(), userFollowed.getId());
        entityManager.clear();

        var updatedUserFollowed = userRepository.findById(userFollowed.getId());
        var updatedUserFollowing = userRepository.findById(userFollowing.getId());

        // Assert
        assertThat(userRepository.count()).isEqualTo(2);
        assertThat(updatedUserFollowed).isPresent();
        assertThat(updatedUserFollowed.get().getFollowers()).isEmpty();
        assertThat(updatedUserFollowed.get().getFollowing()).isEmpty();
        assertThat(updatedUserFollowing).isPresent();
        assertThat(updatedUserFollowing.get().getFollowers()).isEmpty();
        assertThat(updatedUserFollowing.get().getFollowing()).isEmpty();
    }

}
