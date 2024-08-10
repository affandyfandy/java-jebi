package com.fpt.midtemg1.specifications;

import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.entity.Invoice;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

public class InvoiceSpecification implements Specification<Invoice>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final String INVOICE_AMOUNT = "invoiceAmount";
    private static final String CUSTOMER_ID = "customer.id";
    private static final String CUSTOMER_NAME = "customer.name";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String EQUALS_OPERATION = ":";
    private static final String GREATER_THAN_OPERATION = ">";
    private static final String LESS_THAN_OPERATION = "<";

    private transient final SearchCriteria criteria;

    public InvoiceSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        System.out.println("Key: " + criteria.getKey());
        System.out.println("Value: " + criteria.getValue());

        switch (criteria.getKey()) {
            case INVOICE_AMOUNT:
                return handleInvoiceAmount(root, builder);
            case CUSTOMER_ID:
                return handleCustomerId(root, builder);
            case CUSTOMER_NAME:
                return handleCustomerName(root, builder);
            case YEAR:
                return handleYear(root, builder);
            case MONTH:
                return handleMonth(root, builder);
            default:
                return handleDefault(root, builder);
        }
    }

    private Predicate handleInvoiceAmount(Root<Invoice> root, CriteriaBuilder builder) {
        if (GREATER_THAN_OPERATION.equalsIgnoreCase(criteria.getOperation())) {
            return builder.greaterThanOrEqualTo(root.get(INVOICE_AMOUNT), (BigDecimal) criteria.getValue());
        } else if (LESS_THAN_OPERATION.equalsIgnoreCase(criteria.getOperation())) {
            return builder.lessThanOrEqualTo(root.get(INVOICE_AMOUNT), (BigDecimal) criteria.getValue());
        }
        throw new IllegalArgumentException("Invalid operation for invoiceAmount: " + criteria.getOperation());
    }

    private Predicate handleCustomerId(Root<Invoice> root, CriteriaBuilder builder) {
        Join<Invoice, Customer> customerJoin = root.join("customer");
        if (EQUALS_OPERATION.equalsIgnoreCase(criteria.getOperation())) {
            return builder.equal(customerJoin.get("id"), criteria.getValue());
        }
        throw new IllegalArgumentException("Invalid operation for customer.id: " + criteria.getOperation());
    }

    private Predicate handleCustomerName(Root<Invoice> root, CriteriaBuilder builder) {
        Join<Invoice, Customer> customerJoin = root.join("customer");
        if (EQUALS_OPERATION.equalsIgnoreCase(criteria.getOperation())) {
            return builder.like(customerJoin.get("name"), "%" + criteria.getValue() + "%");
        }
        throw new IllegalArgumentException("Invalid operation for customer.name: " + criteria.getOperation());
    }

    private Predicate handleYear(Root<Invoice> root, CriteriaBuilder builder) {
        int year = (int) criteria.getValue();
        Timestamp startOfYear = getStartOfYearTimestamp(year);
        Timestamp endOfYear = getEndOfYearTimestamp(year);
        return builder.between(root.get("invoiceDate"), startOfYear, endOfYear);
    }

    private Predicate handleMonth(Root<Invoice> root, CriteriaBuilder builder) {
        int month = (int) criteria.getValue();
        System.out.println("Month criteria: " + month);
        Timestamp startOfMonth = getStartOfMonthTimestamp(month);
        Timestamp endOfMonth = getEndOfMonthTimestamp(month);
        System.out.println("Start of Month: " + startOfMonth);
        System.out.println("End of Month: " + endOfMonth);
        return builder.between(root.get("invoiceDate"), startOfMonth, endOfMonth);
    }

    private Predicate handleDefault(Root<Invoice> root, CriteriaBuilder builder) {
        if (EQUALS_OPERATION.equalsIgnoreCase(criteria.getOperation())) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        throw new IllegalArgumentException("Invalid search criteria: " + criteria);
    }

    private Timestamp getStartOfYearTimestamp(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return new Timestamp(calendar.getTimeInMillis());
    }

    private Timestamp getEndOfYearTimestamp(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return new Timestamp(calendar.getTimeInMillis());
    }

    private Timestamp getStartOfMonthTimestamp(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(calendar.getTimeInMillis());
    }

    private Timestamp getEndOfMonthTimestamp(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Timestamp(calendar.getTimeInMillis());
    }
}
