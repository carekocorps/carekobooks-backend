package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Forum extends BaseModel {

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
