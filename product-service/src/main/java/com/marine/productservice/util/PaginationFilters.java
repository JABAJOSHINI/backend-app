package com.marine.productservice.util;

import com.marine.productservice.DTO.Filter;
import com.marine.productservice.entity.ClientEntity;
import com.marine.productservice.entity.OrderEntity;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import java.util.*;

@Service
public class PaginationFilters {
    private static final Map<String, String> FIELD_MAPPINGS = Map.of(
            "email", "clientEntity.email",
            "company", "clientEntity.companyAddress",
            "status", "status",
            "orderId", "orderId"
            // add more if needed
    );

    public Predicate buildFilterPredicates(
            List<Filter> filters,
            Root<?> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb
    ) {
        List<Predicate> predicates = new ArrayList<>();
        Map<String, Join<?, ?>> joins = new HashMap<>();

        for (Filter filter : filters) {
            // Map property name to the actual entity attribute path
            String rawProperty = filter.getProperty(); // e.g. "email"
            String mappedProperty = FIELD_MAPPINGS.getOrDefault(rawProperty, rawProperty); // e.g. "clientEntity.email"

            String value = filter.getValue();
            String operator = filter.getOperator();

            // Resolve path automatically with joins for nested attributes
            Path<?> path = resolvePathWithJoins(mappedProperty, root, joins);

            switch (operator.toUpperCase()) {
                case "EQUAL" -> {
                    if (String.class.equals(path.getJavaType())) {
                        predicates.add(cb.like(path.as(String.class), "%" + value.toLowerCase() + "%"));
                    } else {
                        predicates.add(cb.equal(path, value));
                    }
                }
                case "LIKE" ->
                        predicates.add(cb.like(path.as(String.class), "%" + value.toLowerCase() + "%"));
                case "NOT_EQUAL" ->
                        predicates.add(cb.notEqual(path, value));
                case "GREATER_THAN" ->
                        predicates.add(cb.greaterThan(path.as(String.class), value));
                case "LESS_THAN" ->
                        predicates.add(cb.lessThan(path.as(String.class), value));
                default ->
                        throw new IllegalArgumentException("Unsupported operator: " + operator);
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * Automatically resolves nested paths and creates joins if needed.
     */
    private Path<?> resolvePathWithJoins(String propertyPath, Root<?> root, Map<String, Join<?, ?>> joins) {
        String[] parts = propertyPath.split("\\.");
        Path<?> path = root;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (i < parts.length - 1) {
                // This part is a join
                String joinPath = String.join(".", Arrays.copyOfRange(parts, 0, i + 1));
                if (!joins.containsKey(joinPath)) {
                    if (path instanceof Root) {
                        joins.put(joinPath, ((Root<?>) path).join(part, JoinType.LEFT));
                    } else if (path instanceof From) {
                        joins.put(joinPath, ((From<?, ?>) path).join(part, JoinType.LEFT));
                    }
                }
                path = joins.get(joinPath);
            } else {
                // Last part is the actual attribute
                path = path.get(part);
            }
        }

        return path;
    }






//    public Predicate buildFilterPredicates(List<Filter> filters, Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb,  Join<OrderEntity, ClientEntity> clientJoin) {
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        for (Filter filter : filters) {
//            String property = filter.getProperty();
//            String value = filter.getValue();
//            String operator = filter.getOperator();
//
//            Path<String> path;
//
//            // Handle nested clientEntity fields
//            // ✅ Check for nested clientEntity fields safely
//            if (property.startsWith("clientEntity.")) {
//                String nestedField = property.substring("clientEntity.".length());
//                path = clientJoin.get(nestedField);
//            } else {
//                path = root.get(property);
//            }
//
//
//            switch (operator.toUpperCase()) {
//                case "EQUAL" -> predicates.add(cb.equal(path, value));
//                case "LIKE" -> predicates.add(cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
//                case "NOT_EQUAL" -> predicates.add(cb.notEqual(path, value));
//                case "GREATER_THAN" -> predicates.add(cb.greaterThan(path.as(String.class), value));
//                case "LESS_THAN" -> predicates.add(cb.lessThan(path.as(String.class), value));
//                // Extend here if needed
//                default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
//            }
//        }
//
//        return cb.and(predicates.toArray(new Predicate[0]));
//    }
}
