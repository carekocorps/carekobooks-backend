package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Image extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(length = 1024, nullable = false)
    private String url;

    @Column(length = 50, name = "content_type", nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long sizeInBytes;

    @OneToMany(mappedBy = "image")
    private List<User> users;

    @OneToMany(mappedBy = "image")
    private List<Book> books;

}
