package com.fpt.midtemg1.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpt.midtemg1.dto.InvoiceProductDTO;

class InvoiceProductTest {
    private InvoiceProduct invoiceProduct;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();
        product = new Product();
        invoiceProduct = InvoiceProduct.builder()
                .invoiceId("invoice123")
                .productId(1)
                .quantity(5)
                .price(BigDecimal.valueOf(20.00))
                .amount(BigDecimal.valueOf(100.00))
                .invoice(invoice)
                .product(product)
                .createdTime(new Timestamp(System.currentTimeMillis()))
                .updatedTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Test
    void testOnCreate() {
        invoiceProduct.onCreate();
        assertNotNull(invoiceProduct.getCreatedTime());
        assertNotNull(invoiceProduct.getUpdatedTime());
        assertEquals(invoiceProduct.getCreatedTime(), invoiceProduct.getUpdatedTime());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        Timestamp oldTime = invoiceProduct.getUpdatedTime();
        TimeUnit.MILLISECONDS.sleep(10);
        invoiceProduct.preUpdate();
        assertNotNull(invoiceProduct.getUpdatedTime());
        assertNotEquals(oldTime, invoiceProduct.getUpdatedTime());
    }

    @Test
    void testToDTO() {
        InvoiceProductDTO dto = invoiceProduct.toDTO();
        assertEquals(invoiceProduct.getQuantity(), dto.getQuantity());
        assertEquals(invoiceProduct.getPrice(), dto.getPrice());
        assertEquals(invoiceProduct.getAmount(), dto.getAmount());
        assertEquals(invoiceProduct.getCreatedTime(), dto.getCreatedTime());
        assertEquals(invoiceProduct.getUpdatedTime(), dto.getUpdatedTime());
        assertNotNull(dto.getInvoice());
        assertNotNull(dto.getProduct());
    }
}
