package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookGenreSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.BaseApplicationQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class BookGenreQuery extends BaseApplicationQuery<BookGenre> {

    private String name;
    private String displayName;

    @Override
    public Specification<BookGenre> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(name)) specs = specs.and(nameContains(name));
        if (StringUtils.isNotBlank(displayName)) specs = specs.and(displayNameContains(displayName));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "name", "name",
                "displayName", "displayName",
                "createdAt", "createdAt",
                "updatedAt", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "name",
                    "displayName",
                    "createdAt",
                    "updatedAt"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
