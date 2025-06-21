package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.Book;
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
    private Integer pageCountLower;
    private Integer pageCountGreater;

    @Override
    public Specification<Book> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(title)) specs = specs.and(titleContains(title));
        if (StringUtils.isNotBlank(authorName)) specs = specs.and(authorNameContains(authorName));
        if (StringUtils.isNotBlank(publisherName)) specs = specs.and(publisherNameContains(publisherName));
        if (StringUtils.isNotBlank(genre)) specs = specs.and(genreEquals(genre));
        if (publishedBefore != null) specs = specs.and(publishedBefore(publishedBefore));
        if (publishedAfter != null) specs = specs.and(publishedAfter(publishedAfter));
        if (pageCountLower != null) specs = specs.and(pageCountLower(pageCountLower));
        if (pageCountGreater != null) specs = specs.and(pageCountGreater(pageCountGreater));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "title", "title",
                "authorName", "authorName",
                "publisherName", "publisherName",
                "publishedAt", "publishedAt",
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
                    "title",
                    "authorName",
                    "publisherName",
                    "publishedAt",
                    "pageCount",
                    "createdAt",
                    "updatedAt"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
