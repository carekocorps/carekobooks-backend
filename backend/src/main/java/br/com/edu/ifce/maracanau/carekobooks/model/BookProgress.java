package br.com.edu.ifce.maracanau.carekobooks.model;

import br.com.edu.ifce.maracanau.carekobooks.enums.BookProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "book_progress")
public class BookProgress extends AbstractEntity {

    @Column(nullable = false)
    private BookProgressStatus status = BookProgressStatus.READING;

    @Column(nullable = false)
    private Boolean isFavorited = false;

    @Column
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
