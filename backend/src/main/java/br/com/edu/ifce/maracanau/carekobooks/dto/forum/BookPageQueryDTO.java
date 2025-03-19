package br.com.edu.ifce.maracanau.carekobooks.dto.forum;

import br.com.edu.ifce.maracanau.carekobooks.core.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.core.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.core.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.model.Forum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumSearch")
public class BookPageQueryDTO extends BaseApplicationPageQuery<Forum> {

    @Sortable
    @Searchable
    private String title;

}
