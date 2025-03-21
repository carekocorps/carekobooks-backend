package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Search;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForumReplySearchQuery extends BaseApplicationPageQuery<ForumReply> {

    @Search(name = "user.id")
    private Long userId;

    @Search(name = "book.id")
    private Long bookId;

}
