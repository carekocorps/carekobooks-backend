package br.com.edu.ifce.maracanau.carekobooks.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class User extends AbstractEntity {

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
    private List<Activity> activities;

    @OneToMany(mappedBy = "user")
    public List<Thread> threads;

    @OneToMany(mappedBy = "user")
    private List<ThreadReply> replies;

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
