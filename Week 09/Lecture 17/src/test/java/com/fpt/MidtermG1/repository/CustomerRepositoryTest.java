package com.fpt.MidtermG1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Customer;

import com.fpt.MidtermG1.data.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindCustomer() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .phoneNumber("123456789")
                .status(Status.ACTIVE)
                .build();

        customer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getName()).isEqualTo(customer.getName());
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = Customer.builder()
                .name("John Doe")
                .phoneNumber("123456789")
                .status(Status.ACTIVE)
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane Doe")
                .phoneNumber("987654321")
                .status(Status.INACTIVE)
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(2);
    }
}
