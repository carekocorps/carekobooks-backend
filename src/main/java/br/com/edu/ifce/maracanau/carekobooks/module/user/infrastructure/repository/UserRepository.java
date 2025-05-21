package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
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
    void deleteByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.otp = :otp,
            u.otpValidationType = 'EMAIL',
            u.otpExpiresAt = :otpExpiresAt,
            u.tempEmail = :newEmail
        WHERE u.email = :email
    """)
    void changeTempEmailValuesByEmail(String email, String newEmail, String otp, LocalDateTime otpExpiresAt);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.otp = :otp,
            u.otpValidationType = 'PASSWORD',
            u.otpExpiresAt = :otpExpiresAt,
            u.tempPassword = :password
        WHERE u.username = :username
    """)
    void changeTempPasswordValuesByUsername(String username, String password, String otp, LocalDateTime otpExpiresAt);

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
    void verifyUserByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.email = :newEmail,
            u.tempEmail = NULL,
            u.otp = NULL,
            u.otpValidationType = NULL,
            u.otpExpiresAt = NULL
        WHERE u.username = :username
    """)
    void verifyEmailOtpByUsername(String username, String newEmail);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.password = :newPassword,
            u.tempPassword = NULL,
            u.otp = NULL,
            u.otpValidationType = NULL,
            u.otpExpiresAt = NULL
        WHERE u.username = :username
    """)
    void verifyPasswordOtpByUsername(String username, String newPassword);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.username = :newUsername
        WHERE u.username = :username
    """)
    void changeUsernameByUsername(String username, String newUsername);

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
