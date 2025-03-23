package br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.repository.specification;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    public static <T> Specification<T> valueEquals(Object value, String fieldName) {
        return (root, query, cb) ->
                cb.equal(getPath(fieldName, root), value);
    }

    public static <T> Specification<T> valueContains(String value, String fieldName) {
        return (root, query, cb) ->
                cb.like(cb.upper(getPath(fieldName, root)), "%" + value.toUpperCase() + "%");
    }

    private static <T, R> Path<R> getPath(String fieldName, Root<T> root) {
        var pathParts = fieldName.split("\\.");
        Path<R> path = root.get(pathParts[0]);
        for (var i = 1; i < pathParts.length; i++) {
            path = path.get(pathParts[i]);
        }

        return path;
    }

}
