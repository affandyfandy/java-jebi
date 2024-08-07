package com.fpt.MidtermG1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.entity.Invoice;
import com.fpt.MidtermG1.common.Status;

import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.data.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindInvoice() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .phoneNumber("123456789")
                .status(Status.ACTIVE)
                .build();
        customer = customerRepository.save(customer);

        Invoice invoice = Invoice.builder()
                .customer(customer)
                .invoiceAmount(new BigDecimal("500"))
                .invoiceDate(new Timestamp(System.currentTimeMillis()))
                .build();

        invoice = invoiceRepository.save(invoice);

        Invoice foundInvoice = invoiceRepository.findById(invoice.getId()).orElse(null);
        assertThat(foundInvoice).isNotNull();
        assertThat(foundInvoice.getInvoiceAmount()).isEqualTo(invoice.getInvoiceAmount());
    }

    @Test
    void testFindByInvoiceDateBetween() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .phoneNumber("123456789")
                .status(Status.ACTIVE)
                .build();
        customer = customerRepository.save(customer);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Invoice invoice1 = Invoice.builder()
                .customer(customer)
                .invoiceAmount(new BigDecimal("500"))
                .invoiceDate(now)
                .build();

        Invoice invoice2 = Invoice.builder()
                .customer(customer)
                .invoiceAmount(new BigDecimal("300"))
                .invoiceDate(now)
                .build();

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        List<Invoice> invoices = invoiceRepository.findByInvoiceDateBetween(new Timestamp(now.getTime() - 1000), new Timestamp(now.getTime() + 1000));
        assertThat(invoices).hasSize(2);
    }
}
