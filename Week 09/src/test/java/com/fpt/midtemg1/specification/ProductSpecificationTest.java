package com.fpt.midtemg1.specification;

import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.specifications.ProductSpecification;
import com.fpt.midtemg1.specifications.ProductSpecificationsBuilder;
import com.fpt.midtemg1.specifications.SearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductSpecificationTest {

    private ProductSpecification productSpecification;
    private ProductSpecificationsBuilder productSpecificationsBuilder;
    private Root<Product> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    @BeforeEach
    public void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        builder = mock(CriteriaBuilder.class);
    }

    @Test
    public void testToPredicate_greaterThan() {
        // Arrange
        SearchCriteria criteria = new SearchCriteria("price", ">", new BigDecimal("100"));
        productSpecification = new ProductSpecification(criteria);
        Predicate mockPredicate = mock(Predicate.class);
        Path<Object> path = mock(Path.class);

        // Mocking the necessary methods
        when(builder.greaterThan(any(Path.class), any(BigDecimal.class))).thenReturn(mockPredicate);
        when(root.get("price")).thenReturn(path);

        // Act
        Predicate result = productSpecification.toPredicate(root, query, builder);

        // Assert
        assertNotNull(result);
        assertSame(mockPredicate, result);
    }

    @Test
    public void testToPredicate_lessThan() {
        // Arrange
        SearchCriteria criteria = new SearchCriteria("price", "<", new BigDecimal("50"));
        productSpecification = new ProductSpecification(criteria);
        Predicate mockPredicate = mock(Predicate.class);
        Path<Object> path = mock(Path.class);

        // Mocking the necessary methods
        when(builder.lessThan(any(Path.class), any(BigDecimal.class))).thenReturn(mockPredicate);
        when(root.get("price")).thenReturn(path);

        // Act
        Predicate result = productSpecification.toPredicate(root, query, builder);

        // Assert
        assertNotNull(result);
        assertSame(mockPredicate, result);
    }

    @Test
    public void testProductSpecificationsBuilder_empty() {
        // Arrange
        productSpecificationsBuilder = new ProductSpecificationsBuilder();

        // Act
        Specification<Product> result = productSpecificationsBuilder.build();

        // Assert
        assertNull(result);
    }

    @Test
    public void testProductSpecificationsBuilder_withSingleCriterion() {
        // Arrange
        productSpecificationsBuilder = new ProductSpecificationsBuilder();
        productSpecificationsBuilder.with("name", ":", "Product A");

        // Act
        Specification<Product> result = productSpecificationsBuilder.build();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ProductSpecification);
    }
}
