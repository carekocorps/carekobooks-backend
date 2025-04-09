package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.specification.ForumReplySpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationQuery;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class ForumReplyQuery extends BaseApplicationQuery<ForumReply> {

    private String username;
    private Long bookId;

    @Override
    public Specification<ForumReply> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

}
