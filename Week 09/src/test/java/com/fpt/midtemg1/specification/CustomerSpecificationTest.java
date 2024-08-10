package com.fpt.midtemg1.specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fpt.midtemg1.specifications.CustomerSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.fpt.midtemg1.data.entity.Customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerSpecificationTest {

    private Root<Customer> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        builder = mock(CriteriaBuilder.class);
    }

    @Test
    void testToPredicateWithKeyword() {
        // Arrange
        String keyword = "test";
        CustomerSpecification specification = new CustomerSpecification(keyword);

        Predicate namePredicate = mock(Predicate.class);
        Predicate phonePredicate = mock(Predicate.class);
        Predicate statusPredicate = mock(Predicate.class);
        Predicate finalPredicate = mock(Predicate.class);

        when(root.get("name")).thenReturn(mock(jakarta.persistence.criteria.Path.class));
        when(root.get("phoneNumber")).thenReturn(mock(jakarta.persistence.criteria.Path.class));
        when(root.get("status")).thenReturn(mock(jakarta.persistence.criteria.Path.class));

        when(builder.like(root.get("name"), "%test%")).thenReturn(namePredicate);
        when(builder.like(root.get("phoneNumber"), "%test%")).thenReturn(phonePredicate);
        when(builder.like(root.get("status"), "%test%")).thenReturn(statusPredicate);
        when(builder.or(namePredicate, phonePredicate, statusPredicate)).thenReturn(finalPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, builder);

        // Assert
        assertNotNull(result, "The predicate should not be null when keyword is provided.");
    }

    @Test
    void testToPredicateWithoutKeyword() {
        // Arrange
        String keyword = null;
        CustomerSpecification specification = new CustomerSpecification(keyword);

        Predicate conjunctionPredicate = mock(Predicate.class);
        when(builder.conjunction()).thenReturn(conjunctionPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, builder);

        // Assert
        assertNotNull(result, "The predicate should not be null when keyword is null or empty.");
    }
}
