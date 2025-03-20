package br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infra.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infra.model.User;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.enums.BookProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_progress")
public class BookProgress extends BaseModel {

    @Column(nullable = false)
    private BookProgressStatus status = BookProgressStatus.READING;

    @Column(nullable = false)
    private Boolean isFavorited = false;

    @Column
    private Integer score;

    @Column
    private Integer pagesRead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
