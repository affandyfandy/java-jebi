package com.fpt.MidtermG1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.entity.Invoice;
import com.fpt.MidtermG1.data.entity.InvoiceProduct;
import com.fpt.MidtermG1.data.entity.InvoiceProductId;
import com.fpt.MidtermG1.data.entity.Product;
import com.fpt.MidtermG1.common.Status;

import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.data.repository.InvoiceProductRepository;
import com.fpt.MidtermG1.data.repository.InvoiceRepository;
import com.fpt.MidtermG1.data.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class InvoiceProductRepositoryTest {

    @Autowired
    private InvoiceProductRepository invoiceProductRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSaveAndFindInvoiceProduct() {
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

        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();
        product = productRepository.save(product);

        InvoiceProduct invoiceProduct = InvoiceProduct.builder()
                .invoice(invoice)
                .product(product)
                .quantity(2)
                .price(new BigDecimal("1500"))
                .amount(new BigDecimal("3000"))
                .build();
        invoiceProduct = invoiceProductRepository.save(invoiceProduct);

        InvoiceProductId id = new InvoiceProductId(invoice.getId(), product.getId());
        InvoiceProduct foundInvoiceProduct = invoiceProductRepository.findById(id).orElse(null);
        assertThat(foundInvoiceProduct).isNotNull();
        assertThat(foundInvoiceProduct.getQuantity()).isEqualTo(2);
    }

    @Test
    void testDeleteByInvoice() {
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

        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();
        product = productRepository.save(product);

        InvoiceProduct invoiceProduct = InvoiceProduct.builder()
                .invoice(invoice)
                .product(product)
                .quantity(2)
                .price(new BigDecimal("1500"))
                .amount(new BigDecimal("3000"))
                .build();
        invoiceProductRepository.save(invoiceProduct);

        invoiceProductRepository.deleteByInvoice(invoice);
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAll();
        assertThat(invoiceProducts).isEmpty();
    }

    @Test
    void testFindAllWithDetails() {
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

        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();
        product = productRepository.save(product);

        InvoiceProduct invoiceProduct = InvoiceProduct.builder()
                .invoice(invoice)
                .product(product)
                .quantity(2)
                .price(new BigDecimal("1500"))
                .amount(new BigDecimal("3000"))
                .build();
        invoiceProductRepository.save(invoiceProduct);

        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllWithDetails();
        assertThat(invoiceProducts).hasSize(1);
        assertThat(invoiceProducts.get(0).getProduct().getName()).isEqualTo("Laptop");
        assertThat(invoiceProducts.get(0).getInvoice().getCustomer().getName()).isEqualTo("John Doe");
    }
}
