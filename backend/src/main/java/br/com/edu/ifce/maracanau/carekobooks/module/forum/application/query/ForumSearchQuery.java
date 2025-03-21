package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumSearchQuery extends BaseApplicationPageQuery<Forum> {

    @Sortable
    @Searchable
    private String title;

    @Searchable(name = "user.id")
    private Long userId;

    @Searchable(name = "book.id")
    private Long bookId;

}
