package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_activities")
public class BookActivity extends BaseModel {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookProgressStatus status = BookProgressStatus.READING;

    @Column(name = "page_count")
    private Integer pageCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
