package br.com.edu.ifce.maracanau.carekobooks.shared.infrastructure.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    public static <T> Specification<T> valueEquals(Object value, String fieldName) {
        return (root, query, criteriaBuilder) -> {
            if (fieldName.contains(".")) {
                var pathParts = fieldName.split("\\.");
                return criteriaBuilder.equal(root.get(pathParts[0]).get(pathParts[1]), value);
            } else {
                return criteriaBuilder.equal(root.get(fieldName), value);
            }
        };
    }

    public static <T> Specification<T> valueContains(String value, String fieldName) {
        return (root, query, criteriaBuilder) -> {
            if (fieldName.contains(".")) {
                var pathParts = fieldName.split("\\.");
                return criteriaBuilder.like(root.get(pathParts[0]).get(pathParts[1]), "%" + value + "%");
            } else {
                return criteriaBuilder.like(root.get(fieldName), "%" + value + "%");
            }
        };
    }

}
