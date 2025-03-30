package br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.page;

import static br.com.edu.ifce.maracanau.carekobooks.shared.layer.infrastructure.repository.specification.BaseSpecification.*;

import br.com.edu.ifce.maracanau.carekobooks.shared.layer.infrastructure.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public abstract class BaseApplicationPageSearchQuery<T extends BaseModel> extends BaseApplicationPageSearch {

    protected LocalDate createdBefore;
    protected LocalDate createdAfter;

    public Specification<T> getSpecification() {
        Specification<T> specs = Specification.where(null);
        if (createdBefore != null) specs = specs.and(createdBefore(createdBefore));
        if (createdAfter != null) specs = specs.and(createdAfter(createdAfter));
        return specs;
    }

    public Sort getSort() {
        return getSort(Map.of(
                "created-at", "createdAt",
                "updated-at", "updatedAt"
        ));
    }

    protected Sort getSort(Map<String, String> fieldMapping) {
        var direction = isAscendingOrder
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, fieldMapping.getOrDefault(orderBy, "id"));
    }

}
