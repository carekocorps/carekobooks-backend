package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.model.Forum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumSearchQuery")
public class ForumSearchQuery extends BaseApplicationPageQuery<Forum> {

    @Sortable
    @Searchable
    private String title;

}
