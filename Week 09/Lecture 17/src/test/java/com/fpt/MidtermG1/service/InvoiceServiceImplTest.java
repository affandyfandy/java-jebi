package com.fpt.MidtermG1.service;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Customer;
import com.fpt.MidtermG1.data.entity.Invoice;
import com.fpt.MidtermG1.data.entity.Product;
import com.fpt.MidtermG1.data.repository.CustomerRepository;
import com.fpt.MidtermG1.data.repository.InvoiceProductRepository;
import com.fpt.MidtermG1.data.repository.InvoiceRepository;
import com.fpt.MidtermG1.data.repository.ProductRepository;
import com.fpt.MidtermG1.dto.InvoiceDTO;
import com.fpt.MidtermG1.dto.InvoiceProductDTO;
import com.fpt.MidtermG1.dto.ProductDTO;
import com.fpt.MidtermG1.exception.ResourceNotFoundException;
import com.fpt.MidtermG1.service.impl.InvoiceServiceImpl;
import com.fpt.MidtermG1.util.PDFUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

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

    private Customer activeCustomer;
    private Product activeProduct;
    private Product inactiveProduct;
    private InvoiceDTO invoiceDTO;

    @BeforeEach
    void setUp() {
        activeCustomer = Customer.builder()
                .id("1")
                .name("John Doe")
                .status(Status.ACTIVE)
                .build();

        activeProduct = Product.builder()
                .id(1)
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .status(Status.ACTIVE)
                .build();

        inactiveProduct = Product.builder()
                .id(2)
                .name("Product 2")
                .price(BigDecimal.valueOf(200))
                .status(Status.INACTIVE)
                .build();

        InvoiceProductDTO invoiceProductDTO1 = InvoiceProductDTO.builder()
                .product(ProductDTO.builder().id(activeProduct.getId()).build())
                .quantity(2)
                .build();

        InvoiceProductDTO invoiceProductDTO2 = InvoiceProductDTO.builder()
                .product(ProductDTO.builder().id(inactiveProduct.getId()).build())
                .quantity(1)
                .build();

        invoiceDTO = InvoiceDTO.builder()
                .customer(activeCustomer.toDTO())
                .invoiceProducts(Arrays.asList(invoiceProductDTO1, invoiceProductDTO2))
                .build();
    }

    @Test
    void addInvoice_shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });
    }

    @Test
    void addInvoice_shouldThrowExceptionWhenCustomerIsInactive() {
        activeCustomer.setStatus(Status.INACTIVE);
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(activeCustomer));

        assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });
    }

    @Test
    void addInvoice_shouldThrowExceptionWhenProductNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(activeCustomer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });
    }

    @Test
    void addInvoice_shouldThrowExceptionWhenProductIsInactive() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(activeCustomer));
        when(productRepository.findById(activeProduct.getId())).thenReturn(Optional.of(activeProduct));
        when(productRepository.findById(inactiveProduct.getId())).thenReturn(Optional.of(inactiveProduct));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Product " + inactiveProduct.getId() + " is inactive"));
    }

    @Test
    void addInvoice_shouldSaveInvoiceSuccessfully() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(activeCustomer));
        when(productRepository.findById(activeProduct.getId())).thenReturn(Optional.of(activeProduct));
        when(productRepository.findById(inactiveProduct.getId())).thenReturn(Optional.of(activeProduct));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId("1");
            return invoice;
        });

        InvoiceDTO result = invoiceService.addInvoice(invoiceDTO);

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(invoiceProductRepository, times(2)).save(any());
    }
}
