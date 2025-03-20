package br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.BaseApplicationPageSearch;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.query.enums.SearchType;
import br.com.edu.ifce.maracanau.carekobooks.exception.InternalServerException;
import br.com.edu.ifce.maracanau.carekobooks.shared.infra.repository.specification.BaseSpecification;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class BaseApplicationPageQuery<T> extends BaseApplicationPageSearch {

    public Specification<T> getSpecification() {
        List<Specification<T>> specs = new ArrayList<>();
        for (var field : this.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Searchable.class)) {
                try {
                    field.setAccessible(true);
                    var fieldValue = field.get(this);

                    if (fieldValue != null) {
                        var fieldName = field.getName();
                        var fieldSearchType = field.getAnnotation(Searchable.class).type();
                        specs.add(getSpecification(fieldValue, fieldName, fieldSearchType));
                    }
                } catch (IllegalAccessException ex) {
                    throw new InternalServerException("Internal Server Error");
                }
            }
        }

        return specs.isEmpty()
                ? Specification.where(null)
                : specs.stream().reduce(Specification::and).get();
    }

    public Sort getSort() {
        var sortField = "id";
        for (var field : this.getClass().getDeclaredFields()) {
            var annotation = field.getAnnotation(Sortable.class);
            if (annotation != null) {
                var fieldName = field.getName();
                var annotationName = annotation.name().isEmpty()
                        ? fieldName
                        : annotation.name();

                if (annotationName.equals(orderBy)) {
                    sortField = fieldName;
                    break;
                }
            }
        }

        var direction = isAscendingOrder
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortField);
    }

    private Specification<T> getSpecification(Object fieldValue, String fieldName, SearchType searchType) {
        if (searchType == SearchType.TEXT_CONTAINS && !(fieldValue instanceof String)) {
            throw new InternalServerException("Internal Server Error");
        }

        return searchType == SearchType.VALUE_EQUALS
                ? BaseSpecification.valueEquals(fieldValue, fieldName)
                : BaseSpecification.valueContains((String) fieldValue, fieldName);
    }

}
