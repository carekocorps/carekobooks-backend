package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query;

import static br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.specification.BookGenreSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.BaseApplicationCachedQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
@Setter
public class BookGenreQuery extends BaseApplicationCachedQuery<BookGenre> {

    private String name;
    private String friendlyName;

    @Override
    public Specification<BookGenre> getSpecification() {
        var specs = super.getSpecification();
        if (StringUtils.isNotBlank(name)) specs = specs.and(nameContains(name));
        if (StringUtils.isNotBlank(friendlyName)) specs = specs.and(friendlyNameContains(friendlyName));
        return specs;
    }

    @Override
    public Sort getSort() {
        return getSort(Map.of(
                "name", "name",
                "friendly-name", "friendlyName",
                "created-at", "createdAt",
                "updated-at", "updatedAt"
        ));
    }

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "name",
                    "friendly-name",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

    @Override
    public boolean isDefaultCacheSearch() {
        return super.isDefaultCacheSearch()
                && name == null
                && friendlyName == null;
    }

}
