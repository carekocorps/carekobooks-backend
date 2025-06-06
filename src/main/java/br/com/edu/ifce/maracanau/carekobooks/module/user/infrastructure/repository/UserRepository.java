package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findByUsernameIn(List<String> usernames);
    Optional<User> findByKeycloakId(UUID keycloakId);
    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO user_follow_relationships (user_following_id, user_followed_id) VALUES
            (:followingId, :followedId)
    """, nativeQuery = true)
    void follow(Long followingId, Long followedId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM user_follow_relationships
        WHERE user_following_id = :followingId
        AND user_followed_id = :followedId
    """, nativeQuery = true)
    void unfollow(Long followingId, Long followedId);

}
