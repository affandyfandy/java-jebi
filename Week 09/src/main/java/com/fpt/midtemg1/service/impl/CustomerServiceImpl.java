package com.fpt.midtemg1.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.repository.CustomerRepository;
import com.fpt.midtemg1.dto.CustomerDTO;
import com.fpt.midtemg1.exception.CustomerNotFoundException;
import com.fpt.midtemg1.exception.CustomerStatusException;
import com.fpt.midtemg1.service.CustomerService;
import com.fpt.midtemg1.specifications.CustomerSpecification;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found with id: ";

    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerDTO> getCustomerList(Pageable pageable) {
        return customerRepository.findAll(pageable)
                                 .map(Customer::toDTO);
    }

    @Override
    public Page<CustomerDTO> searchCustomers(String keyword, Pageable pageable) {
        return customerRepository.findAll(new CustomerSpecification(keyword), pageable)
                                 .map(Customer::toDTO);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(String id) {
        return customerRepository.findById(id)
                                 .map(Customer::toDTO);
    }

    @Override
    public CustomerDTO addCustomer(@Valid CustomerDTO customerDTO) {
        return customerRepository.save(customerDTO.toEntity())
                                 .toDTO();
    }

    @Override
    public CustomerDTO editCustomer(String id, @Valid CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                                              .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id));

        if (customerDTO.getName() != null) customer.setName(customerDTO.getName());
        if (customerDTO.getPhoneNumber() != null) customer.setPhoneNumber(customerDTO.getPhoneNumber());
        if (customerDTO.getStatus() != null) customer.setStatus(customerDTO.getStatus());

        return customerRepository.save(customer)
                                 .toDTO();
    }

    @Override
    public CustomerDTO activateCustomer(String id) {
        return updateCustomerStatus(id, Status.ACTIVE, Status.INACTIVE);
    }

    @Override
    public CustomerDTO deactivateCustomer(String id) {
        return updateCustomerStatus(id, Status.INACTIVE, Status.ACTIVE);
    }

    private CustomerDTO updateCustomerStatus(String id, Status newStatus, Status requiredCurrentStatus) {
        Customer customer = customerRepository.findById(id)
                                              .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE + id));

        if (customer.getStatus() != requiredCurrentStatus) {
            throw new CustomerStatusException("Customer status is already " + customer.getStatus());
        }

        customer.setStatus(newStatus);
        return customerRepository.save(customer)
                                 .toDTO();
    }
}
