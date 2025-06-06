package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseEntity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThreadReply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_keycloak_id", columnList = "keycloakId", unique = true),
                @Index(name = "idx_username", columnList = "username", unique = true)
        }
)
public class User extends BaseEntity {

    @Column(name = "keycloak_id", unique = true, nullable = false)
    private UUID keycloakId;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "display_name", length = 50)
    private String displayName;

    @Column(length = 1000)
    private String description;

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

    @Transactional(readOnly = true)
    public Integer getProgressesCount() {
        return Optional.ofNullable(progresses).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getActivitiesCount() {
        return Optional.ofNullable(activities).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getReviewsCount() {
        return Optional.ofNullable(reviews).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getThreadsCount() {
        return Optional.ofNullable(threads).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getRepliesCount() {
        return Optional.ofNullable(replies).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getFollowingCount() {
        return Optional.ofNullable(following).map(List::size).orElse(0);
    }

    @Transactional(readOnly = true)
    public Integer getFollowersCount() {
        return Optional.ofNullable(followers).map(List::size).orElse(0);
    }

}
