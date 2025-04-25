package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.OtpValidationType;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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

    @Column(nullable = false)
    private String password;

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

}
