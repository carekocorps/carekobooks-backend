package br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.BaseApplicationPageSearch;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.annotation.Sortable;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.enums.SearchType;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.InternalServerException;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.repository.specification.BaseSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplicationPageQuery<T extends BaseModel> extends BaseApplicationPageSearch {

    public Specification<T> getSpecification() {
        List<Specification<T>> specs = new ArrayList<>();
        for (var field : this.getClass().getDeclaredFields()) {
            var annotation = field.getAnnotation(Searchable.class);
            if (annotation != null) {
                try {
                    field.setAccessible(true);
                    var fieldValue = field.get(this);
                    if (fieldValue != null) {
                        var fieldName = field.getName();
                        var annotationName = annotation.name().isEmpty()
                                ? fieldName
                                : annotation.name();

                        var fieldSearchType = annotation.type();
                        specs.add(getSpecification(fieldValue, annotationName, fieldSearchType));
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
        var targetField = "id";
        switch (orderBy) {
            case "id" -> {}
            case "created-at" -> targetField = "createdAt";
            case "updated-at" -> targetField = "updatedAt";
            default -> {
                for (var field : this.getClass().getDeclaredFields()) {
                    var annotation = field.getAnnotation(Sortable.class);
                    if (annotation != null) {
                        var fieldName = field.getName();
                        var annotationName = annotation.name().isEmpty()
                                ? fieldName
                                : annotation.name();

                        if (annotationName.equals(orderBy)) {
                            targetField = fieldName;
                            break;
                        }
                    }
                }
            }
        }

        var direction = isAscendingOrder
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, targetField);
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
