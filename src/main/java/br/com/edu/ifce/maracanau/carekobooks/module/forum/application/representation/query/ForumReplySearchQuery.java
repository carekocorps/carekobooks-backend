package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository.specification.ForumReplySpecification.*;
import static br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.repository.specification.BaseSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.BaseApplicationPageSearchQuery;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Getter
@Setter
public class ForumReplySearchQuery extends BaseApplicationPageSearchQuery<ForumReply> {

    private String username;
    private LocalDate createdBefore;
    private LocalDate createdAfter;
    private Long bookId;

    @Override
    public Specification<ForumReply> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (createdBefore != null) specs = specs.and(createdBefore(createdBefore));
        if (createdAfter != null) specs = specs.and(createdAfter(createdAfter));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

}
