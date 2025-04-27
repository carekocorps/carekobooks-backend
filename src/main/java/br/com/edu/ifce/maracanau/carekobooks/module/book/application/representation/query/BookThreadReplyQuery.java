package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookThreadReplySpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationQuery;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class BookThreadReplyQuery extends BaseApplicationQuery<BookThreadReply> {

    private Long parentId;
    private String username;
    private Long bookId;

    @Override
    public Specification<BookThreadReply> getSpecification() {
        var specs = super.getSpecification();
        if (parentId != null) specs = specs.and(parentIdEqual(parentId));
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

}
