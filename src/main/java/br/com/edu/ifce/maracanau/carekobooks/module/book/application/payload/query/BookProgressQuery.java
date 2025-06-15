package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookProgressSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;
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
public class BookProgressQuery extends BaseApplicationQuery<BookProgress> {

    private String username;
    private String genre;
    private BookProgressStatus status;
    private Boolean isFavorite;
    private Integer score;
    private Integer pageCount;
    private Long bookId;

    @Override
    public Specification<BookProgress> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(username)) specs = specs.and(usernameEqual(username));
        if (StringUtils.isNotBlank(genre)) specs = specs.and(genreEqual(genre));
        if (status != null) specs = specs.and(statusEqual(status));
        if (isFavorite != null) specs = specs.and(isFavoriteEqual(isFavorite));
        if (score != null) specs = specs.and(scoreEqual(score));
        if (pageCount != null) specs = specs.and(pageCountEqual(pageCount));
        if (bookId != null) specs = specs.and(bookIdEqual(bookId));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "username", "user.username",
                "status", "status",
                "isFavorite", "isFavorite",
                "score", "score",
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
                    "isFavorite",
                    "score",
                    "pageCount",
                    "createdAt",
                    "updatedAt"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
