package br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.infra.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.model.Forum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Book extends BaseModel {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String synopsis;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @Column(name = "total_pages", nullable = false)
    private Integer totalPages;

    @Column(name = "score_sum", nullable = false)
    private Integer scoreSum = 0;

    @Column(name = "score_count", nullable = false)
    private Integer scoreCount = 0;

    @OneToMany(mappedBy = "book")
    private List<BookProgress> progresses;

    @OneToMany(mappedBy = "book")
    private List<BookActivity> activities;

    @OneToMany(mappedBy = "book")
    private List<Forum> forums;

}
