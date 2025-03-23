package br.com.edu.ifce.maracanau.carekobooks.shared.application.page;

import br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.model.BaseModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public abstract class BaseApplicationPageSearchQuery<T extends BaseModel> extends BaseApplicationPageSearch {

    public Specification<T> getSpecification() {
        return Specification.where(null);
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
