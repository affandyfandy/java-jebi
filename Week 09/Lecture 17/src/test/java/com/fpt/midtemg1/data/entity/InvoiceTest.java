package com.fpt.midtemg1.data.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fpt.midtemg1.dto.InvoiceDTO;

public class InvoiceTest {
    private Invoice invoice;
    private Customer customer;
    private Set<InvoiceProduct> invoiceProducts;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        invoiceProducts = new HashSet<>();
        invoice = Invoice.builder()
                .id("12345")
                .customer(customer)
                .invoiceAmount(BigDecimal.valueOf(100.00))
                .invoiceDate(new Timestamp(System.currentTimeMillis()))
                .createdTime(new Timestamp(System.currentTimeMillis()))
                .updatedTime(new Timestamp(System.currentTimeMillis()))
                .invoiceProducts(invoiceProducts)
                .build();
    }

    @Test
    public void testOnCreate() {
        invoice.onCreate();
        assertNotNull(invoice.getCreatedTime());
        assertNotNull(invoice.getUpdatedTime());
        assertEquals(invoice.getCreatedTime(), invoice.getUpdatedTime());
    }

    @Test
    public void testPreUpdate() throws InterruptedException {
        Timestamp oldTime = invoice.getUpdatedTime();
        TimeUnit.MILLISECONDS.sleep(10);
        invoice.preUpdate();
        assertNotNull(invoice.getUpdatedTime());
        assertNotEquals(oldTime, invoice.getUpdatedTime());
    }

    @Test
    public void testToDTO() {
        InvoiceDTO dto = invoice.toDTO();
        assertEquals(invoice.getId(), dto.getId());
        assertEquals(invoice.getCustomer().toDTO(), dto.getCustomer());
        assertEquals(invoice.getInvoiceAmount(), dto.getInvoiceAmount());
        assertEquals(invoice.getInvoiceDate(), dto.getInvoiceDate());
        assertEquals(invoice.getCreatedTime(), dto.getCreatedTime());
        assertEquals(invoice.getUpdatedTime(), dto.getUpdatedTime());
        assertNotNull(dto.getInvoiceProducts());
    }
}
