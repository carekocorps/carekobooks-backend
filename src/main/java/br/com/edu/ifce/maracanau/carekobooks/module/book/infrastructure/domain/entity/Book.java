package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;
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
@Table(name = "books")
public class Book extends BaseModel {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String synopsis;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "publisher_name", nullable = false)
    private String publisherName;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @Column(name = "page_count", nullable = false)
    private Integer pageCount;

    @Column(name = "user_average_score")
    private Double userAverageScore;

    @Column(name = "review_average_score")
    private Double reviewAverageScore;

    @ManyToOne
    @JoinColumn(name = "image_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre_relationships",
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
    private List<BookThread> threads;

}
