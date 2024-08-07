package com.fpt.MidtermG1.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.fpt.MidtermG1.dto.CustomerDTO;
import com.fpt.MidtermG1.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.entity.Invoice;
import com.fpt.MidtermG1.data.entity.InvoiceProduct;
import com.fpt.MidtermG1.data.entity.Product;
import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.data.repository.InvoiceProductRepository;
import com.fpt.MidtermG1.data.repository.InvoiceRepository;
import com.fpt.MidtermG1.data.repository.ProductRepository;
import com.fpt.MidtermG1.dto.InvoiceDTO;
import com.fpt.MidtermG1.dto.InvoiceProductDTO;
import com.fpt.MidtermG1.dto.ProductDTO;
import com.fpt.MidtermG1.exception.ResourceNotFoundException;
import com.fpt.MidtermG1.util.PDFUtils;

@DataJpaTest
public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InvoiceProductRepository invoiceProductRepository;

    @Mock
    private PDFUtils pdfUtils;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddInvoice_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("cust-1");
        customer.setStatus(Status.ACTIVE);

        Product product = new Product();
        product.setId(1);
        product.setPrice(BigDecimal.valueOf(100));
        product.setStatus(Status.ACTIVE);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setProduct(ProductDTO.builder().id(1).build());
        invoiceProductDTO.setQuantity(2);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomer(new CustomerDTO());
        invoiceDTO.setInvoiceProducts(List.of(invoiceProductDTO));

        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice savedInvoice = invocation.getArgument(0);
            savedInvoice.setId("inv-1");
            return savedInvoice;
        });

        // Act
        InvoiceDTO result = invoiceService.addInvoice(invoiceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(200), result.getInvoiceAmount());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void testAddInvoice_InactiveCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("cust-1");
        customer.setStatus(Status.INACTIVE);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomer(new CustomerDTO());

        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(customer));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    public void testAddInvoice_ProductNotFound() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("cust-1");
        customer.setStatus(Status.ACTIVE);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setProduct(ProductDTO.builder().id(1).build());

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomer(new CustomerDTO());
        invoiceDTO.setInvoiceProducts(List.of(invoiceProductDTO));

        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    public void testEditInvoice_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("cust-1");
        customer.setStatus(Status.ACTIVE);

        Product product = new Product();
        product.setId(1);
        product.setPrice(BigDecimal.valueOf(100));
        product.setStatus(Status.ACTIVE);

        InvoiceProductDTO invoiceProductDTO = new InvoiceProductDTO();
        invoiceProductDTO.setProduct(ProductDTO.builder().id(1).build());
        invoiceProductDTO.setQuantity(2);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomer(new CustomerDTO());
        invoiceDTO.setInvoiceProducts(List.of(invoiceProductDTO));

        Invoice existingInvoice = new Invoice();
        existingInvoice.setId("inv-1");
        existingInvoice.setCustomer(customer);
        existingInvoice.setInvoiceDate(Timestamp.from(Instant.now().minus(Duration.ofMinutes(5))));

        when(invoiceRepository.findById("inv-1")).thenReturn(Optional.of(existingInvoice));
        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(customer));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(existingInvoice);

        // Act
        InvoiceDTO result = invoiceService.editInvoice("inv-1", invoiceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(200), result.getInvoiceAmount());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void testEditInvoice_InvoiceNotFound() {
        // Arrange
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        when(invoiceRepository.findById("inv-1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.editInvoice("inv-1", invoiceDTO);
        });

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    public void testEditInvoice_InactiveCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setId("cust-1");
        customer.setStatus(Status.INACTIVE);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomer(new CustomerDTO());

        Invoice existingInvoice = new Invoice();
        existingInvoice.setId("inv-1");
        existingInvoice.setCustomer(customer);
        existingInvoice.setInvoiceDate(Timestamp.from(Instant.now().minus(Duration.ofMinutes(5))));

        when(invoiceRepository.findById("inv-1")).thenReturn(Optional.of(existingInvoice));
        when(customerRepository.findById("cust-1")).thenReturn(Optional.of(customer));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            invoiceService.editInvoice("inv-1", invoiceDTO);
        });

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}
