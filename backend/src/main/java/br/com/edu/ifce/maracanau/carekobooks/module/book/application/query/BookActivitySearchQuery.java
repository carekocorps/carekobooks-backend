package br.com.edu.ifce.maracanau.carekobooks.module.book.application.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookActivityStatus;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookActivitySearchQuery extends BaseApplicationPageQuery<BookActivity> {

    @Sortable
    @Searchable
    private BookActivityStatus status;

    @Sortable(name = "pages-read")
    @Searchable
    private Integer pagesRead;

    @Searchable(name = "user.id")
    private Long userId;

    @Searchable(name = "user.id")
    private Long bookId;

}
