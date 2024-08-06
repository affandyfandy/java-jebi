package com.fpt.MidtermG1.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.dto.CustomerDTO;
import com.fpt.MidtermG1.exception.ResourceNotFoundException;
import com.fpt.MidtermG1.service.impl.CustomerServiceImpl;

public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId("1");
        customer.setName("John Doe");
        customer.setPhoneNumber("123456789");
        customer.setStatus(Status.ACTIVE);

        customerDTO = new CustomerDTO();
        customerDTO.setId("1");
        customerDTO.setName("John Doe");
        customerDTO.setPhoneNumber("123456789");
        customerDTO.setStatus(Status.ACTIVE);
    }

    @Test
    public void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.addCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testEditCustomer() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setName("Jane Doe");
        updatedCustomerDTO.setPhoneNumber("987654321");

        CustomerDTO result = customerService.editCustomer("1", updatedCustomerDTO);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("987654321", result.getPhoneNumber());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testActivateCustomer() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.activateCustomer("1");

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testDeactivateCustomer() {
        customer.setStatus(Status.ACTIVE);
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.deactivateCustomer("1");

        assertNotNull(result);
        assertEquals(Status.INACTIVE, result.getStatus());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testActivateCustomerNotFound() {
        when(customerRepository.findById("2")).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.activateCustomer("2");
        });

        assertEquals("Customer not found with id: 2", thrown.getMessage());
    }

    @Test
    public void testEditCustomerNotFound() {
        when(customerRepository.findById("2")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            customerService.editCustomer("2", customerDTO);
        });

        assertEquals("Customer not found with id: 2", thrown.getMessage());
    }
}
