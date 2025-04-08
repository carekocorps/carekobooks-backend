package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookActivitySpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationPageSearchQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class BookActivitySearchQuery extends BaseApplicationPageSearchQuery<BookActivity> {

    private String username;
    private BookProgressStatus status;
    private Integer pagesRead;
    private Long bookId;

    @Override
    public Specification<BookActivity> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (status != null) specs = specs.and(statusEqual(status));
        if (pagesRead != null) specs = specs.and(pagesReadEqual(pagesRead));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "user.username",
                "status", "status",
                "pages-read", "pagesRead",
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
                    "status",
                    "pages-read",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
