package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.enums.UserRole;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel implements UserDetails {

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(length = 50)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "verification_token")
    private UUID verificationToken;

    @Column(name = "verification_token_expires_at")
    private LocalDateTime verificationTokenExpiresAt;

    @Column(name = "password_verification_token")
    private UUID passwordVerificationToken;

    @Column(name = "password_verification_token_expires_at")
    private LocalDateTime passwordVerificationTokenExpiresAt;

    @Column(name = "email_verification_token")
    private UUID emailVerificationToken;

    @Column(name = "email_verification_token_expires_at")
    private LocalDateTime emailVerificationTokenExpiresAt;

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
    private List<Forum> forums;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ForumReply> replies;

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
