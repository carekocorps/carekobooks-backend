package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class BookQuery extends BaseApplicationQuery<Book> {

    private String title;
    private String authorName;
    private String publisherName;
    private String genre;
    private LocalDate publishedBefore;
    private LocalDate publishedAfter;
    private Integer pageCount;

    @Override
    public Specification<Book> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(authorName)) specs = specs.and(authorNameContains(authorName));
        if (StringUtils.isNotBlank(publisherName)) specs = specs.and(publisherNameContains(publisherName));
        if (StringUtils.isNotBlank(title)) specs = specs.and(titleContains(title));
        if (StringUtils.isNotBlank(genre)) specs = specs.and(genreEquals(genre));
        if (publishedBefore != null) specs = specs.and(publishedBefore(publishedBefore));
        if (publishedAfter != null) specs = specs.and(publishedAfter(publishedAfter));
        if (pageCount != null) specs = specs.and(pageCountEquals(pageCount));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "title", "title",
                "author-name", "authorName",
                "publisher-name", "publisherName",
                "published-at", "publishedAt",
                "page-count", "pageCount",
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
                    "author-name",
                    "publisher-name",
                    "published-at",
                    "page-count",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
