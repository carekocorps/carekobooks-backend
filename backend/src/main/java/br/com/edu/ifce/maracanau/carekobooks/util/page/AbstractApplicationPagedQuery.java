package br.com.edu.ifce.maracanau.carekobooks.util.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Getter
@Setter
public abstract class AbstractApplicationPagedQuery<T> extends AbstractApplicationPagedSearch {

    public abstract Specification<T> getSpecification();
    public abstract Sort getSort();

    protected Specification<T> getEmptySpecification() {
        return Specification.where(null);
    }

    protected Sort getSort(List<String> fields) {
        var sortField = fields.contains(orderBy)
                ? orderBy
                : "id";

        var direction = isAscendingOrder
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortField);
    }

}
