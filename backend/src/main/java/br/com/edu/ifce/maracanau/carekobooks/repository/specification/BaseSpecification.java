package br.com.edu.ifce.maracanau.carekobooks.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    public static <T> Specification<T> valueEquals(Object value, String valueName) {
        return (root, query, cb) ->
                cb.equal(root.get(valueName), value);
    }

    public static <T> Specification<T> valueContains(String value, String valueName) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get(valueName)), "%" + value.toUpperCase() + "%");
    }

}
