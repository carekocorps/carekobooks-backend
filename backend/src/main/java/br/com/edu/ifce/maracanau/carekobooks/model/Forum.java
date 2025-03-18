package br.com.edu.ifce.maracanau.carekobooks.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Forum extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToMany(mappedBy = "forum")
    private List<ForumReply> replies;

}
