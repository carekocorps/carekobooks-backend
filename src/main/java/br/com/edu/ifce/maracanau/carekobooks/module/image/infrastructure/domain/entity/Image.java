package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;
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
@Table(name = "images")
public class Image extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(length = 1024, nullable = false)
    private String url;

    @Column(name = "content_type", length = 50, nullable = false)
    private String contentType;

    @Column(name = "size_in_bytes", nullable = false)
    private Long sizeInBytes;

    @OneToMany(mappedBy = "image")
    private List<User> users;

    @OneToMany(mappedBy = "image")
    private List<Book> books;

}
