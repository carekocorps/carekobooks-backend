package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookActivitySpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
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
public class BookActivityQuery extends BaseApplicationQuery<BookActivity> {

    private String username;
    private String genre;
    private BookProgressStatus status;
    private Integer pageCount;
    private Long bookId;

    @Override
    public Specification<BookActivity> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (StringUtils.isNotBlank(genre)) specs = specs.and(genreEqual(genre));
        if (status != null) specs = specs.and(statusEqual(status));
        if (pageCount != null) specs = specs.and(pageCountEqual(pageCount));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "user.username",
                "status", "status",
                "pageCount", "pageCount",
                "createdAt", "createdAt",
                "updatedAt", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "username",
                    "status",
                    "pageCount",
                    "createdAt",
                    "updatedAt"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
