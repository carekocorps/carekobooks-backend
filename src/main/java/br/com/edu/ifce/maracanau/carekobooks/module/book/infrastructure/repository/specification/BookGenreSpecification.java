package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import org.springframework.data.jpa.domain.Specification;

public class BookGenreSpecification {

    private BookGenreSpecification() {
    }

    public static Specification<BookGenre> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<BookGenre> displayNameContains(String displayName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("displayName")), "%" + displayName.toUpperCase() + "%");
    }

}
