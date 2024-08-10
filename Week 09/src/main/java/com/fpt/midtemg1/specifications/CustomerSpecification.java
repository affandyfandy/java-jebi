package com.fpt.midtemg1.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fpt.midtemg1.data.entity.Customer;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerSpecification implements Specification<Customer> {

    private final String keyword;

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return builder.conjunction(); // Always return a valid predicate
        }

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.like(root.get("name"), "%" + keyword + "%"));
        predicates.add(builder.like(root.get("phoneNumber"), "%" + keyword + "%"));
        predicates.add(builder.like(root.get("status"), "%" + keyword + "%"));

        return builder.or(predicates.toArray(new Predicate[0]));
    }
}

