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
    void deleteByUsername(String username);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.resetToken = :resetToken,
            u.resetTokenExpiresAt = :expiresAt
        WHERE u.email = :email
    """)
    void updateResetTokenByEmail(String email, UUID resetToken, LocalDateTime expiresAt);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.password = :password,
            u.resetToken = NULL,
            u.resetTokenExpiresAt = NULL
        WHERE u.email = :email
    """)
    void resetPasswordByEmail(String email, String password);

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u
        SET u.isEnabled = true,
            u.verificationToken = null,
            u.verificationTokenExpiresAt = null
        WHERE u.email = :email
    """)
    void verifyUserByEmail(String email);

}
