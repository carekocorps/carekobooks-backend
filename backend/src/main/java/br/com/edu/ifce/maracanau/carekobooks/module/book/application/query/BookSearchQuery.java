package br.com.edu.ifce.maracanau.carekobooks.module.book.application.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.enums.SearchType;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "BookSearchQuery")
public class BookSearchQuery extends BaseApplicationPageQuery<Book> {

    @Sortable
    @Searchable(type = SearchType.TEXT_CONTAINS)
    private String title;

    @Sortable
    @Searchable(type = SearchType.TEXT_CONTAINS)
    private String author;

    @Searchable(type = SearchType.TEXT_CONTAINS)
    private String publisher;

    @Sortable(name = "published-at")
    @Searchable
    private LocalDate publishedAt;

    @Sortable(name = "total-pages")
    @Searchable
    private Integer totalPages;

}
