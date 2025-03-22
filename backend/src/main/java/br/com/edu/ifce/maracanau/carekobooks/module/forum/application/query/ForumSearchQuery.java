package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "title",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
