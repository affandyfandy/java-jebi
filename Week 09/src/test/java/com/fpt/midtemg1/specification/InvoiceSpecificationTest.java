package com.fpt.midtemg1.specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.entity.Invoice;
import com.fpt.midtemg1.specifications.InvoiceSpecification;
import com.fpt.midtemg1.specifications.InvoiceSpecificationsBuilder;
import com.fpt.midtemg1.specifications.SearchCriteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

class InvoiceSpecificationTest {

    @Mock
    private Root<Invoice> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @InjectMocks
    private InvoiceSpecification invoiceSpecification;

    public InvoiceSpecificationTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToPredicate_invoiceAmountGreaterThan() {
        SearchCriteria criteria = new SearchCriteria("invoiceAmount", ">", new BigDecimal("1000"));
        invoiceSpecification = new InvoiceSpecification(criteria);

        Path<BigDecimal> path = mock(Path.class);
        doReturn(path).when(root).get("invoiceAmount");
        doReturn(mock(Predicate.class)).when(builder).greaterThanOrEqualTo(eq(path), eq(new BigDecimal("1000")));

        Predicate predicate = invoiceSpecification.toPredicate(root, query, builder);
        assertNotNull(predicate);
    }

    @Test
    void testToPredicate_invoiceAmountLessThan() {
        SearchCriteria criteria = new SearchCriteria("invoiceAmount", "<", new BigDecimal("1000"));
        invoiceSpecification = new InvoiceSpecification(criteria);

        Path<BigDecimal> path = mock(Path.class);
        doReturn(path).when(root).get("invoiceAmount");
        doReturn(mock(Predicate.class)).when(builder).lessThanOrEqualTo(eq(path), eq(new BigDecimal("1000")));

        Predicate predicate = invoiceSpecification.toPredicate(root, query, builder);
        assertNotNull(predicate);
    }

    @Test
    void testToPredicate_customerId() {
        SearchCriteria criteria = new SearchCriteria("customer.id", ":", "12345");
        invoiceSpecification = new InvoiceSpecification(criteria);

        Join<Invoice, Customer> customerJoin = mock(Join.class);
        Path<String> path = mock(Path.class);

        doReturn(customerJoin).when(root).join("customer");
        doReturn(path).when(customerJoin).get("id");
        doReturn(mock(Predicate.class)).when(builder).equal(eq(path), eq("12345"));

        Predicate predicate = invoiceSpecification.toPredicate(root, query, builder);
        assertNotNull(predicate);
    }

    @Test
    void testToPredicate_customerName() {
        SearchCriteria criteria = new SearchCriteria("customer.name", ":", "John Doe");
        invoiceSpecification = new InvoiceSpecification(criteria);

        Join<Invoice, Customer> customerJoin = mock(Join.class);
        Path<String> path = mock(Path.class);

        doReturn(customerJoin).when(root).join("customer");
        doReturn(path).when(customerJoin).get("name");
        doReturn(mock(Predicate.class)).when(builder).like(eq(path), eq("%John Doe%"));

        Predicate predicate = invoiceSpecification.toPredicate(root, query, builder);
        assertNotNull(predicate);
    }

    @Test
    void testBuild_singleSpecification() {
        InvoiceSpecificationsBuilder builder = new InvoiceSpecificationsBuilder();
        builder.with("invoiceAmount", ">", new BigDecimal("1000"));

        Specification<Invoice> specification = builder.build();
        assertNotNull(specification);
    }

    @Test
    void testBuild_multipleSpecifications() {
        InvoiceSpecificationsBuilder builder = new InvoiceSpecificationsBuilder();
        builder.with("invoiceAmount", ">", new BigDecimal("1000"))
                .with("customer.name", ":", "John Doe");

        Specification<Invoice> specification = builder.build();
        assertNotNull(specification);
    }
}
