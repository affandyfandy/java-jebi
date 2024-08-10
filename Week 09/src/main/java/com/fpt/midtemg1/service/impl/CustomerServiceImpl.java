package com.fpt.midtemg1.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.repository.CustomerRepository;
import com.fpt.midtemg1.specifications.CustomerSpecification;
import com.fpt.midtemg1.dto.CustomerDTO;
import com.fpt.midtemg1.exception.CustomerNotFoundException;
import com.fpt.midtemg1.exception.CustomerStatusException;
import com.fpt.midtemg1.service.CustomerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found with id: ";

    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerDTO> getCustomerList(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(Customer::toDTO);
    }

    @Override
    public Page<CustomerDTO> searchCustomers(String keyword, Pageable pageable) {
        CustomerSpecification specification = new CustomerSpecification(keyword);
        Page<Customer> customers = customerRepository.findAll(specification, pageable);
        return customers.map(Customer::toDTO);
    }

    @Override
    public Optional<CustomerDTO> getCusromerById(String id) {
        return customerRepository.findById(id).map(Customer::toDTO);
    }

    @Override
    public CustomerDTO addCustomer(@Valid CustomerDTO body) {
        Customer response = customerRepository.save(body.toEntity());
        return response.toDTO();
    }

    @Override
    public CustomerDTO editCustomer(String id, @Valid CustomerDTO body) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id));

        if (body.getName() != null) {
            customer.setName(body.getName());
        }

        if (body.getPhoneNumber() != null) {
            customer.setPhoneNumber(body.getPhoneNumber());
        }

        if (body.getStatus() != null) {
            customer.setStatus(body.getStatus());
        }

        customer = customerRepository.save(customer);
        return customer.toDTO();
    }

    @Override
    public CustomerDTO activateCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id));

        if (customer.getStatus() == Status.INACTIVE) {
            customer.setStatus(Status.ACTIVE);
        } else {
            throw new CustomerStatusException("Customer status already " + customer.getStatus());
        }

        return customerRepository.save(customer).toDTO();
    }

    @Override
    public CustomerDTO deactivateCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id));

        if (customer.getStatus() == Status.ACTIVE) {
            customer.setStatus(Status.INACTIVE);
        } else {
            throw new CustomerStatusException("Customer status already " + customer.getStatus());
        }

        return customerRepository.save(customer).toDTO();
    }
}
