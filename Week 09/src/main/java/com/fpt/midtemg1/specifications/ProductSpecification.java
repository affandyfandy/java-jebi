package com.fpt.midtemg1.specifications;

import com.fpt.midtemg1.data.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.math.BigDecimal;

public class ProductSpecification implements Specification<Product> {

    private final transient SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThan(root.get(criteria.getKey()), (BigDecimal) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThan(root.get(criteria.getKey()), (BigDecimal) criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue().toString());
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
