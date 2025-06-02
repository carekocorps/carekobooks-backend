package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.OtpValidationType;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRole;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_email", columnList = "email")
        }
)
public class User extends BaseModel implements UserDetails {

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "temp_email")
    private String tempEmail;

    @Column(nullable = false)
    private String password;

    @Column(name = "temp_password")
    private String tempPassword;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(length = 8)
    private String otp;

    @Column(name = "otp_validation_type")
    @Enumerated(EnumType.STRING)
    private OtpValidationType otpValidationType;

    @Column(name = "otp_expires_at")
    private LocalDateTime otpExpiresAt;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Image image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BookProgress> progresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BookActivity> activities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BookReview> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BookThread> threads;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BookThreadReply> replies;

    @ManyToMany
    @JoinTable(
            name = "user_follow_relationships",
            joinColumns = @JoinColumn(name = "user_following_id"),
            inverseJoinColumns = @JoinColumn(name = "user_followed_id")
    )
    private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

    public List<String> getRoles() {
        return getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var userAuthority = new SimpleGrantedAuthority("ROLE_USER");
        var adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        return role == UserRole.USER
                ? List.of(userAuthority)
                : List.of(userAuthority, adminAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Transactional(readOnly = true)
    public Integer getProgressesCount() {
        return Optional.ofNullable(progresses).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getActivitiesCount() {
        return Optional.ofNullable(activities).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getReviewsCount() {
        return Optional.ofNullable(reviews).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getThreadsCount() {
        return Optional.ofNullable(threads).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getRepliesCount() {
        return Optional.ofNullable(replies).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getFollowingCount() {
        return Optional.ofNullable(following).map(List::size).orElse(null);
    }

    @Transactional(readOnly = true)
    public Integer getFollowersCount() {
        return Optional.ofNullable(followers).map(List::size).orElse(null);
    }

}
