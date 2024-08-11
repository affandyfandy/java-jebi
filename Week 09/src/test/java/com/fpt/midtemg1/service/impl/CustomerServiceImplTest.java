package com.fpt.midtemg1.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.repository.CustomerRepository;
import com.fpt.midtemg1.dto.CustomerDTO;
import com.fpt.midtemg1.exception.CustomerNotFoundException;
import com.fpt.midtemg1.exception.CustomerStatusException;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName("John Doe");
        customer.setPhoneNumber("123456789");
        customer.setStatus(Status.ACTIVE);

        customerDTO = customer.toDTO();
    }

    @Test
    void testGetCustomerList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.getCustomerList(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
    }

    @Test
    void testSearchCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        Page<Customer> customerPage = new PageImpl<>(customerList, pageable, 1);

        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.searchCustomers("John", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        Optional<CustomerDTO> result = customerService.getCustomerById(customer.getId());

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = customerService.getCustomerById("nonexistent-id");

        assertFalse(result.isPresent());
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.addCustomer(customerDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testEditCustomer() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.editCustomer(customer.getId(), customerDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testEditCustomer_NotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.editCustomer("nonexistent-id", customerDTO));
    }

    @Test
    void testActivateCustomer() {
        customer.setStatus(Status.INACTIVE);
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.activateCustomer(customer.getId());

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    void testActivateCustomer_AlreadyActive() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        assertThrows(CustomerStatusException.class, () -> customerService.activateCustomer(customer.getId()));
    }

    @Test
    void testDeactivateCustomer() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.deactivateCustomer(customer.getId());

        assertNotNull(result);
        assertEquals(Status.INACTIVE, result.getStatus());
    }

    @Test
    void testDeactivateCustomer_AlreadyInactive() {
        customer.setStatus(Status.INACTIVE);
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        assertThrows(CustomerStatusException.class, () -> customerService.deactivateCustomer(customer.getId()));
    }
}
