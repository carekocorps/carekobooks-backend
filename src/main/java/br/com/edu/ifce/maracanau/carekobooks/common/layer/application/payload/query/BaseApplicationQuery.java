package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query;

import static br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.repository.specification.BaseSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.BaseApplicationSearch;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public abstract class BaseApplicationQuery<T extends BaseEntity> extends BaseApplicationSearch {

    protected LocalDate createdBefore;
    protected LocalDate createdAfter;

    public Specification<T> getSpecification() {
        Specification<T> specs = (root, query, cb) -> cb.conjunction();
        if (createdBefore != null) specs = specs.and(createdBefore(createdBefore));
        if (createdAfter != null) specs = specs.and(createdAfter(createdAfter));
        return specs;
    }

    public Sort getSort() {
        return getSort(Map.of(
                "createdAt", "createdAt",
                "updatedAt", "updatedAt"
        ));
    }

    protected Sort getSort(Map<String, String> fieldMapping) {
        var direction = Boolean.TRUE.equals(isAscendingOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, fieldMapping.getOrDefault(orderBy, "id"));
    }

}
