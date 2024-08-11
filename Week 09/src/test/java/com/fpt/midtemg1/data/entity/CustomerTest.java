package com.fpt.midtemg1.data.entity;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.dto.CustomerDTO;

class CustomerTest {
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id("12345")
                .name("John Doe")
                .phoneNumber("1234567890")
                .status(Status.ACTIVE)
                .createdTime(new Timestamp(System.currentTimeMillis()))
                .updatedTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Test
    void testOnCreate() {
        customer.onCreate();
        assertNotNull(customer.getCreatedTime());
        assertNotNull(customer.getUpdatedTime());
        assertEquals(customer.getCreatedTime(), customer.getUpdatedTime());
    }

    @Test
    void testPreUpdate() throws InterruptedException {
        Timestamp oldTime = customer.getUpdatedTime();
        TimeUnit.MILLISECONDS.sleep(10);
        customer.preUpdate();
        Timestamp newTime = customer.getUpdatedTime();
        assertNotNull(newTime, "Updated time should not be null");
        assertNotEquals(oldTime, newTime, "Updated time should be different from old time");
    }

    @Test
    void testToDTO() {
        CustomerDTO dto = customer.toDTO();
        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getName(), dto.getName());
        assertEquals(customer.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(customer.getStatus(), dto.getStatus());
        assertEquals(customer.getCreatedTime(), dto.getCreatedTime());
        assertEquals(customer.getUpdatedTime(), dto.getUpdatedTime());
    }
}
