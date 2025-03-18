package br.com.edu.ifce.maracanau.carekobooks.dto.forum;

import br.com.edu.ifce.maracanau.carekobooks.core.page.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.core.page.annotations.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.core.page.annotations.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.model.Forum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumSearch")
public class ForumSearchDTO extends BaseApplicationQuery<Forum> {

    @Sortable
    @Searchable
    private String title;

}
