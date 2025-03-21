package br.com.edu.ifce.maracanau.carekobooks.module.book.application.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Search;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressSearchQuery extends BaseApplicationPageQuery<BookProgress> {

    @Search(name = "user.id")
    private Long userId;

    @Search(name = "book.id")
    private Long bookId;

}
