package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findByUsernameIn(List<String> usernames);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.otp = NULL,
            u.otpValidationType = NULL,
            u.otpExpiresAt = NULL,
            u.isEnabled = TRUE
        WHERE u.username = :username
    """)
    void verifyByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.otp = :otp,
            u.otpValidationType = :otpValidationType,
            u.otpExpiresAt = :otpExpiresAt
        WHERE u.username = :username
    """)
    void changeOtpValuesByUsername(String username, String otp, OtpValidationType otpValidationType, LocalDateTime otpExpiresAt);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.password = :password,
            u.otp = NULL,
            u.otpValidationType = NULL,
            u.otpExpiresAt = NULL
        WHERE u.username = :username
    """)
    void changePasswordByUsername(String username, String password);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.email = :email,
            u.otp = NULL,
            u.otpValidationType = NULL,
            u.otpExpiresAt = NULL
        WHERE u.username = :username
    """)
    void changeEmailByUsername(String username, String email);

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
