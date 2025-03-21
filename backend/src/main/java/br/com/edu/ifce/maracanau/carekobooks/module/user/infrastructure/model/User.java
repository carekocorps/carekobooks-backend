package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class User extends BaseModel {

    @Column(length = 50, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "user")
    private List<BookProgress> progresses;

    @OneToMany(mappedBy = "user")
    private List<BookActivity> activities;

    @OneToMany(mappedBy = "user")
    public List<Forum> forums;

    @OneToMany(mappedBy = "user")
    private List<ForumReply> replies;

    @ManyToMany
    @JoinTable(
            name = "user_follow_relationship",
            joinColumns = @JoinColumn(name = "user_following_id"),
            inverseJoinColumns = @JoinColumn(name = "user_followed_id")
    )
    private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;

}
