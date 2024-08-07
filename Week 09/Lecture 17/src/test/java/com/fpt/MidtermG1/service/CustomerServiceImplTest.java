package com.fpt.MidtermG1.service;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.data.specification.CustomerSpecification;
import com.fpt.MidtermG1.dto.CustomerDTO;
import com.fpt.MidtermG1.exception.ResourceNotFoundException;
import com.fpt.MidtermG1.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId("1");
        customer.setName("Test Customer");
        customer.setPhoneNumber("1234567890");
        customer.setStatus(Status.ACTIVE);

        customerDTO = new CustomerDTO();
        customerDTO.setId("1");
        customerDTO.setName("Test Customer");
        customerDTO.setPhoneNumber("1234567890");
        customerDTO.setStatus(Status.ACTIVE);
    }

    @Test
    void testGetCustomerList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.getCustomerList(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSearchCustomers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        CustomerSpecification specification = new CustomerSpecification("Test");

        when(customerRepository.findAll(any(CustomerSpecification.class), eq(pageable))).thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.searchCustomers("Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(any(CustomerSpecification.class), eq(pageable));
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

        Optional<CustomerDTO> result = customerService.getCusromerById("1");

        assertTrue(result.isPresent());
        assertEquals(customerDTO.getName(), result.get().getName());
        verify(customerRepository, times(1)).findById("1");
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById("1")).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = customerService.getCusromerById("1");

        assertFalse(result.isPresent());
    }

    @Test
    void testAddCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.addCustomer(customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testEditCustomer() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.editCustomer("1", customerDTO);

        assertNotNull(result);
        assertEquals(customerDTO.getName(), result.getName());
        verify(customerRepository, times(1)).findById("1");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testEditCustomer_NotFound() {
        when(customerRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.editCustomer("1", customerDTO));
    }

    @Test
    void testActivateCustomer() {
        customer.setStatus(Status.INACTIVE);
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.activateCustomer("1");

        assertEquals(Status.ACTIVE, result.getStatus());
        verify(customerRepository, times(1)).findById("1");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testActivateCustomer_AlreadyActive() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

        assertThrows(RuntimeException.class, () -> customerService.activateCustomer("1"));
    }

    @Test
    void testDeactivateCustomer() {
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.deactivateCustomer("1");

        assertEquals(Status.INACTIVE, result.getStatus());
        verify(customerRepository, times(1)).findById("1");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeactivateCustomer_AlreadyInactive() {
        customer.setStatus(Status.INACTIVE);
        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

        assertThrows(RuntimeException.class, () -> customerService.deactivateCustomer("1"));
    }
}
