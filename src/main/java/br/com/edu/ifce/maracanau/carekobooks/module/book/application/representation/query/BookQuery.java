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
    private String author;
    private String publisher;
    private String genre;
    private LocalDate publishedBefore;
    private LocalDate publishedAfter;
    private Integer totalPages;

    @Override
    public Specification<Book> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(author)) specs = specs.and(authorContains(author));
        if (StringUtils.isNotBlank(publisher)) specs = specs.and(publisherContains(publisher));
        if (StringUtils.isNotBlank(title)) specs = specs.and(titleContains(title));
        if (StringUtils.isNotBlank(genre)) specs = specs.and(genreEquals(genre));
        if (publishedBefore != null) specs = specs.and(publishedBefore(publishedBefore));
        if (publishedAfter != null) specs = specs.and(publishedAfter(publishedAfter));
        if (totalPages != null) specs = specs.and(totalPagesEqual(totalPages));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "title", "title",
                "author", "author",
                "publisher", "publisher",
                "published-at", "publishedAt",
                "total-pages", "totalPages",
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
                    "author",
                    "publisher",
                    "published-at",
                    "total-pages",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
