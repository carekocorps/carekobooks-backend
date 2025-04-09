package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.specification.ForumSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.Forum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class ForumQuery extends BaseApplicationQuery<Forum> {

    private String title;
    private String username;
    private Long bookId;

    @Override
    public Specification<Forum> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(title)) specs = specs.and(titleContains(title));
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "title", "title",
                "created-at", "createdAt",
                "updated-at", "updatedAt"
        ));
    }

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
