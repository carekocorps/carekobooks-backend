package br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infra.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infra.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.Book;
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
