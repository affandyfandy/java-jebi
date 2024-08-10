package com.fpt.midtemg1.data.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import com.fpt.midtemg1.data.entity.Invoice;
import com.fpt.midtemg1.data.entity.InvoiceProduct;
import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.dto.InvoiceProductDTO;

public class InvoiceProductTest {
    private InvoiceProduct invoiceProduct;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    public void setUp() {
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
    public void testOnCreate() {
        invoiceProduct.onCreate();
        assertNotNull(invoiceProduct.getCreatedTime());
        assertNotNull(invoiceProduct.getUpdatedTime());
        assertEquals(invoiceProduct.getCreatedTime(), invoiceProduct.getUpdatedTime());
    }

    @Test
    public void testPreUpdate() throws InterruptedException {
        Timestamp oldTime = invoiceProduct.getUpdatedTime();
        TimeUnit.MILLISECONDS.sleep(10);
        invoiceProduct.preUpdate();
        assertNotNull(invoiceProduct.getUpdatedTime());
        assertNotEquals(oldTime, invoiceProduct.getUpdatedTime());
    }

    @Test
    public void testToDTO() {
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
