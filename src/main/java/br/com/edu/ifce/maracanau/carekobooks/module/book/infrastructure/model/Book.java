package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "user_average_score")
    private Double userAverageScore;

    @Column(name = "review_average_socre")
    private Double reviewAverageScore;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre_relationship",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<BookGenre> genres;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookProgress> progresses;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookActivity> activities;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookReview> reviews;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Forum> forums;

}
