package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findByUsernameIn(List<String> usernames);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.passwordVerificationToken = :token,
            u.passwordVerificationTokenExpiresAt = :expiresAt
        WHERE u.username = :username
    """)
    void initPasswordRecovery(String username, UUID token, LocalDateTime expiresAt);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.emailVerificationToken = :token,
            u.emailVerificationTokenExpiresAt = :expiresAt
        WHERE u.username = :username
    """)
    void initEmailChange(String username, UUID token, LocalDateTime expiresAt);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.isEnabled = true,
            u.verificationToken = null,
            u.verificationTokenExpiresAt = null
        WHERE u.username = :username
    """)
    void verifyRegistration(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.password = :password,
            u.passwordVerificationToken = NULL,
            u.passwordVerificationTokenExpiresAt = NULL
        WHERE u.username = :username
    """)
    void verifyPasswordRecovery(String username, String password);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.email = :email,
            u.emailVerificationToken = NULL,
            u.emailVerificationTokenExpiresAt = NULL
        WHERE u.username = :username
    """)
    void verifyEmailChange(String username, String email);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO `user_follow_relationships` (`user_following_id`, `user_followed_id`) VALUES
            (:userFollowingId, :userFollowedId);
    """, nativeQuery = true)
    void follow(Long userFollowingId, Long userFollowedId);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM `user_follow_relationships`
        WHERE `user_following_id` = :userFollowingId
        AND `user_followed_id` = :userFollowedId;
    """, nativeQuery = true)
    void unfollow(Long userFollowingId, Long userFollowedId);

}
