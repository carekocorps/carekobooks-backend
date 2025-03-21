package br.com.edu.ifce.maracanau.carekobooks.module.book.application.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Search;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sort;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.enums.SearchType;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookSearchQuery extends BaseApplicationPageQuery<Book> {

    @Sort
    @Search(type = SearchType.TEXT_CONTAINS)
    private String title;

    @Sort
    @Search(type = SearchType.TEXT_CONTAINS)
    private String author;

    @Search(type = SearchType.TEXT_CONTAINS)
    private String publisher;

    @Sort(name = "published-at")
    @Search
    private LocalDate publishedAt;

    @Sort(name = "total-pages")
    @Search
    private Integer totalPages;

}
