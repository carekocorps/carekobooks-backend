package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_genres")
public class BookGenre extends BaseModel {

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "friendly_name", length = 100, nullable = false)
    private String friendlyName;

    @Column
    private String description;

    @ManyToMany(mappedBy = "genres")
    private List<Book> books;

}
