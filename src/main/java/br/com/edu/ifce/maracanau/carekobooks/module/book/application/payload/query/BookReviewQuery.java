package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookReviewSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookReview;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class BookReviewQuery extends BaseApplicationQuery<BookReview> {

    private String username;
    private Integer score;
    private Long bookId;

    @Override
    public Specification<BookReview> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (score != null) specs = specs.and(scoreEqual(score));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "user.username",
                "score", "score",
                "created-at", "createdAt",
                "updated-at", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "username",
                    "score",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
