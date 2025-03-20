package br.com.edu.ifce.maracanau.carekobooks.module.activity.infra.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infra.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infra.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Activity extends BaseModel {

    @Column
    private BookProgressStatus status;

    @Column
    private Integer pagesRead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
