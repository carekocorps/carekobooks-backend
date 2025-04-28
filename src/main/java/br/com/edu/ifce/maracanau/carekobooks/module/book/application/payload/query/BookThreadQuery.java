package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookThreadSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThread;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class BookThreadQuery extends BaseApplicationQuery<BookThread> {

    private String title;
    private String username;
    private Long bookId;

    @Override
    public Specification<BookThread> getSpecification() {
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
