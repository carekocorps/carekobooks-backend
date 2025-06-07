package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseEntity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_progresses")
public class BookProgress extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookProgressStatus status = BookProgressStatus.READING;

    @Column(name = "is_favorite", nullable = false)
    private Boolean isFavorite = false;

    @Column
    private Integer score;

    @Column(name = "page_count")
    private Integer pageCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
