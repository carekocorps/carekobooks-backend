package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_activity")
public class BookActivity extends BaseModel {

    @Column(nullable = false)
    private BookProgressStatus status = BookProgressStatus.READING;

    @Column(name = "pages_read")
    private Integer pagesRead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
