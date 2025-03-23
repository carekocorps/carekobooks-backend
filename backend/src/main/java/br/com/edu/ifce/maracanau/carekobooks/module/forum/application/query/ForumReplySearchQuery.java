package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.annotation.Searchable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumReplySearchQuery extends BaseApplicationPageQuery<ForumReply> {

    @Searchable(name = "user.id")
    private Long userId;

    @Searchable(name = "book.id")
    private Long bookId;

}
