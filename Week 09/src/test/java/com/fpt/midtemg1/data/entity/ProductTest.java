package com.fpt.midtemg1.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.dto.ProductDTO;

class ProductTest {
    private Product product;
    private Set<InvoiceProduct> invoiceProducts;

    @BeforeEach
    void setUp() {
        invoiceProducts = new HashSet<>();
        product = Product.builder()
                .id(1)
                .name("Product Name")
                .price(BigDecimal.valueOf(50.00))
                .status(Status.ACTIVE)
                .createdTime(new Timestamp(System.currentTimeMillis()))
                .updatedTime(new Timestamp(System.currentTimeMillis()))
                .invoiceProducts(invoiceProducts)
                .build();
    }

    @Test
    void testOnCreate() {
        product.onCreate();
        assertNotNull(product.getCreatedTime());
        assertNotNull(product.getUpdatedTime());
        assertEquals(product.getCreatedTime(), product.getUpdatedTime());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        Timestamp oldTime = product.getUpdatedTime();
        TimeUnit.MILLISECONDS.sleep(10);
        product.preUpdate();
        assertNotNull(product.getUpdatedTime());
        assertNotEquals(oldTime, product.getUpdatedTime());
    }


    @Test
    void testToDTO() {
        ProductDTO dto = product.toDTO();
        assertEquals(product.getId(), dto.getId());
        assertEquals(product.getName(), dto.getName());
        assertEquals(product.getPrice(), dto.getPrice());
        assertEquals(product.getStatus(), dto.getStatus());
        assertEquals(product.getCreatedTime(), dto.getCreatedTime());
        assertEquals(product.getUpdatedTime(), dto.getUpdatedTime());
        assertNotNull(dto.getInvoiceProducts());
    }
}
