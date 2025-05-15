package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "book_genres",
        indexes = {
                @Index(name = "idx_name", columnList = "name")
        }
)
public class BookGenre extends BaseModel {

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "display_name", length = 100, nullable = false)
    private String displayName;

    @Column
    private String description;

    @ManyToMany(mappedBy = "genres")
    private List<Book> books;

    @PreRemove
    private void preRemove() {
        books.forEach(book -> book.getGenres().remove(this));
    }

}
