package br.com.edu.ifce.maracanau.carekobooks.util.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Hashtable;

@Getter
@Setter
public abstract class AbstractCustomPagedQuery<T> extends AbstractCustomPagedSearch {

    public abstract Specification<T> getSpecification();
    public abstract Sort getSort();

    protected Specification<T> getEmptySpecification() {
        return Specification.where(null);
    }

    protected Sort getSort(Hashtable<String, String> sortableFields) {
        var sortField = sortableFields.containsKey(orderBy)
                ? orderBy
                : "id";

        var direction = isAscendent
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortField);
    }

}
