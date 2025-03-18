package br.com.edu.ifce.maracanau.carekobooks.dto.book;

import br.com.edu.ifce.maracanau.carekobooks.core.page.annotations.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.core.page.annotations.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.core.page.enums.SearchType;
import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.core.page.BaseApplicationQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "BookSearch")
public class BookSearchDTO extends BaseApplicationQuery<Book> {

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
