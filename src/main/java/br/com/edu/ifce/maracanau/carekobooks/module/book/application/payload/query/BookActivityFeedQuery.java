package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookActivitySpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class BookActivityFeedQuery extends BaseApplicationQuery<BookActivity> {

    @NotBlank
    private String username;

    @Override
    public Specification<BookActivity> getSpecification() {
        return super.getSpecification().and(followerUsernameEqual(username));
    }

}
