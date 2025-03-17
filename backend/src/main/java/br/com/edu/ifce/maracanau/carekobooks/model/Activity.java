package br.com.edu.ifce.maracanau.carekobooks.model;

import br.com.edu.ifce.maracanau.carekobooks.enums.BookProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Activity extends AbstractEntity {

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
