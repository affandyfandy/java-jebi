package com.fpt.midtemg1.service.impl;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Customer;
import com.fpt.midtemg1.data.entity.Invoice;
import com.fpt.midtemg1.data.entity.InvoiceProduct;
import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.data.repository.CustomerRepository;
import com.fpt.midtemg1.data.repository.InvoiceProductRepository;
import com.fpt.midtemg1.data.repository.InvoiceRepository;
import com.fpt.midtemg1.data.repository.ProductRepository;
import com.fpt.midtemg1.dto.*;
import com.fpt.midtemg1.exception.ResourceNotFoundException;
import com.fpt.midtemg1.util.PDFUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

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

    @Captor
    private ArgumentCaptor<Invoice> invoiceCaptor;

    private Customer customer;
    private Product product;
    private InvoiceDTO invoiceDTO;
    private InvoiceProductDTO invoiceProductDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId("C001");
        customer.setName("John Doe");
        customer.setPhoneNumber("1234567890");
        customer.setStatus(Status.ACTIVE);
        customer.setCreatedTime(Timestamp.from(Instant.now()));
        customer.setUpdatedTime(Timestamp.from(Instant.now()));

        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStatus(Status.ACTIVE);
        product.setCreatedTime(Timestamp.from(Instant.now()));
        product.setUpdatedTime(Timestamp.from(Instant.now()));

        invoiceProductDTO = InvoiceProductDTO.builder()
                .product(ProductDTO.builder()
                        .id(1)
                        .name("Test Product")
                        .price(BigDecimal.valueOf(100.00))
                        .status(Status.ACTIVE)
                        .build())
                .quantity(2)
                .price(BigDecimal.valueOf(100.00))
                .amount(BigDecimal.valueOf(200.00))
                .build();

        invoiceDTO = InvoiceDTO.builder()
                .id("I001")
                .customer(CustomerDTO.builder()
                        .id("C001")
                        .name("John Doe")
                        .phoneNumber("1234567890")
                        .status(Status.ACTIVE)
                        .build())
                .invoiceAmount(BigDecimal.valueOf(200.00))
                .invoiceProducts(Collections.singletonList(invoiceProductDTO))
                .build();
    }

    @Test
    void testAddInvoiceSuccess() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId("I001");
            return invoice;
        });

        InvoiceDTO savedInvoiceDTO = invoiceService.addInvoice(invoiceDTO);

        assertNotNull(savedInvoiceDTO);
        assertEquals(invoiceDTO.getCustomer().getId(), savedInvoiceDTO.getCustomer().getId());
        assertEquals(200.00, savedInvoiceDTO.getInvoiceAmount().doubleValue());

        verify(invoiceRepository, times(1)).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();
        assertEquals(invoiceDTO.getInvoiceProducts().size(), savedInvoice.getInvoiceProducts().size());
    }

    @Test
    void testAddInvoiceProductNotFound() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testAddInvoiceCustomerNotFound() {
        // Mock the productRepository to return an active product
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        // Mock the customerRepository to return an empty Optional (customer not found)
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        // Execute the service method and assert the exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        // Assert that the exception message is "Customer not found"
        assertEquals("Customer not found", exception.getMessage());
    }


    @Test
    void testAddInvoiceInactiveCustomer() {
        // Mock the productRepository to return an active product
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        // Set customer status to INACTIVE
        customer.setStatus(Status.INACTIVE);
        // Mock the customerRepository to return the INACTIVE customer
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        // Execute the service method and assert the ResponseStatusException
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        // Assert that the exception status is BAD_REQUEST and the reason is "Customer is inactive"
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Customer is inactive", exception.getReason());
    }


    @Test
    void testAddInvoiceInactiveProduct() {
        // Set product status to INACTIVE
        product.setStatus(Status.INACTIVE);
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Product 1 is inactive", exception.getReason());
    }

    @Test
    void testEditInvoiceSuccess() {
        // Create and set up mock entities
        Invoice existingInvoice = invoiceDTO.toEntity();
        existingInvoice.setInvoiceDate(Timestamp.from(Instant.now().minusSeconds(300))); // within 10 minutes

        // Mock repositories
        when(invoiceRepository.findById(anyString())).thenReturn(Optional.of(existingInvoice));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        // Initialize captor
        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);

        // Mock save operation to return the saved invoice
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the service method
        InvoiceDTO updatedInvoiceDTO = invoiceService.editInvoice("I001", invoiceDTO);

        // Verify results
        assertNotNull(updatedInvoiceDTO);
        assertEquals(invoiceDTO.getCustomer().getId(), updatedInvoiceDTO.getCustomer().getId());
        assertEquals(200.00, updatedInvoiceDTO.getInvoiceAmount().doubleValue());

        // Verify and capture saved invoice
        verify(invoiceRepository, times(1)).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();
        assertNotNull(savedInvoice);
        assertEquals(invoiceDTO.getInvoiceProducts().size(), savedInvoice.getInvoiceProducts().size());
    }

    @Test
    void testEditInvoiceExpired() {
        Invoice existingInvoice = invoiceDTO.toEntity();
        existingInvoice.setInvoiceDate(Timestamp.from(Instant.now().minusSeconds(900))); // past 10 minutes
        when(invoiceRepository.findById(anyString())).thenReturn(Optional.of(existingInvoice));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.editInvoice("I001", invoiceDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invoice can only be edited within 10 minutes of creation", exception.getReason());
    }

    @Test
    void testGetInvoiceByIdSuccess() {
        Invoice invoice = invoiceDTO.toEntity();
        when(invoiceRepository.findById(anyString())).thenReturn(Optional.of(invoice));

        InvoiceDTO foundInvoiceDTO = invoiceService.getInvoiceById("I001");

        assertNotNull(foundInvoiceDTO);
        assertEquals(invoiceDTO.getId(), foundInvoiceDTO.getId());
    }

    @Test
    void testGetInvoiceByIdNotFound() {
        when(invoiceRepository.findById(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.getInvoiceById("I001");
        });

        assertEquals("Invoice not found with id: I001", exception.getMessage());
    }


    @Test
    void testExportAllInvoicesToPDFFailure() throws IOException {
        when(pdfUtils.generateAllInvoicesPDF(any())).thenThrow(new IOException("PDF generation error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.exportAllInvoicesToPDF();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to export the PDF: PDF generation error", exception.getReason());
    }

    @Test
    void testGenerateAllInvoicesPDF() throws IOException {
        // Mock data
        List<InvoiceProduct> invoiceProducts = Arrays.asList(
                createInvoiceProduct("1", BigDecimal.valueOf(200.00)),
                createInvoiceProduct("2", BigDecimal.valueOf(300.00))
        );

        byte[] expectedPdfBytes = new byte[]{1, 2, 3, 4, 5};

        // Mock PDFUtils behavior
        when(pdfUtils.generateAllInvoicesPDF(invoiceProducts)).thenReturn(expectedPdfBytes);

        // Directly test the PDF generation
        byte[] actualPdfBytes = pdfUtils.generateAllInvoicesPDF(invoiceProducts);

        // Verify the results
        assertNotNull(actualPdfBytes);
        assertArrayEquals(expectedPdfBytes, actualPdfBytes);
    }



    @Test
    void testGetRevenueByPeriodNoInvoices() {
        when(invoiceRepository.findByInvoiceDateBetween(any(), any())).thenReturn(new ArrayList<>());

        List<RevenueReportDTO> report = invoiceService.getRevenueByPeriod(2024, 8, 10);

        assertTrue(report.isEmpty());
    }

    @Test
    void testExportAllInvoicesToPDFWithVariedProducts() throws IOException {
        List<InvoiceProduct> invoiceProducts = Arrays.asList(
                createInvoiceProduct("1", BigDecimal.valueOf(200.00)),
                createInvoiceProduct("2", BigDecimal.valueOf(300.00))
        );

        byte[] pdfBytes = new byte[] {1, 2, 3, 4, 5};
        when(pdfUtils.generateAllInvoicesPDF(anyList())).thenReturn(pdfBytes);

        byte[] resultPdfBytes = invoiceService.exportAllInvoicesToPDF();

        assertNotNull(resultPdfBytes);
        assertArrayEquals(pdfBytes, resultPdfBytes);
    }

    private InvoiceProduct createInvoiceProduct(String invoiceId, BigDecimal amount) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        invoiceProduct.setInvoiceId(invoiceId);
        invoiceProduct.setAmount(amount);
        return invoiceProduct;
    }

    @Test
    void testGetInvoiceByIdThrowsException() {
        when(invoiceRepository.findById(anyString())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> invoiceService.getInvoiceById("invalidId"));
    }

    @Test
    void testGetRevenueByPeriodWithInvoices() {
        // Prepare mock data
        Invoice invoice = new Invoice();
        invoice.setInvoiceAmount(BigDecimal.valueOf(200.00));
        invoice.setInvoiceDate(Timestamp.valueOf(LocalDateTime.now()));

        when(invoiceRepository.findByInvoiceDateBetween(any(), any())).thenReturn(Collections.singletonList(invoice));

        // Call the method under test
        List<RevenueReportDTO> report = invoiceService.getRevenueByPeriod(2024, 8, 10);

        // Assert the results
        assertNotNull(report);
        assertFalse(report.isEmpty());
        assertEquals(1, report.size());
        assertEquals(BigDecimal.valueOf(200.00), report.get(0).getRevenue());
    }

    @Test
    void testAddInvoiceWithNoProducts() {
        // Prepare invoiceDTO with no products
        invoiceDTO.setInvoiceProducts(new ArrayList<>());

        // Mock customer and product repositories
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        // Execute the service method and assert the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        // Assert that the exception status is BAD_REQUEST and the reason is "Invoice must have at least one product"
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invoice must have at least one product", exception.getReason());
    }

    @Test
    void testAddInvoiceWithNoCustomer() {
        // Mock the productRepository to return an active product
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        // Mock the customerRepository to return an empty Optional (customer not found)
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        // Execute the service method and assert the exception
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        // Assert that the exception message is "Customer not found"
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void testAddInvoiceWithInvalidAmount() {
        invoiceDTO.setInvoiceAmount(BigDecimal.valueOf(-100.00)); // Invalid amount

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            invoiceService.addInvoice(invoiceDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid invoice amount", exception.getReason());
    }

    @Test
    void testGetRevenueByPeriodDifferentDateRanges() {
        LocalDate today = LocalDate.now();
        Invoice invoice = new Invoice();
        invoice.setInvoiceAmount(BigDecimal.valueOf(200.00));
        invoice.setInvoiceDate(Timestamp.valueOf(today.atStartOfDay()));

        when(invoiceRepository.findByInvoiceDateBetween(any(), any())).thenReturn(Collections.singletonList(invoice));

        List<RevenueReportDTO> report = invoiceService.getRevenueByPeriod(today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        assertNotNull(report);
        assertFalse(report.isEmpty());
        assertEquals(1, report.size());
        assertEquals(BigDecimal.valueOf(200.00), report.get(0).getRevenue());
    }

    @Test
    void testEditInvoiceNotFound() {
        when(invoiceRepository.findById(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            invoiceService.editInvoice("invalidId", invoiceDTO);
        });

        assertEquals("Invoice not found with id: invalidId", exception.getMessage());
    }

    @Test
    void testGetInvoiceByCriteriaSuccess() {
        // Prepare mock data
        Invoice invoice = new Invoice();
        invoice.setId("I001");
        invoice.setCustomer(customer);
        invoice.setInvoiceAmount(BigDecimal.valueOf(200.00));
        invoice.setInvoiceDate(Timestamp.from(Instant.now()));

        List<Invoice> invoices = Collections.singletonList(invoice);

        // Set up the search criteria
        String customerId = "C001";
        String customerName = null; // or set a value if needed
        int year = 0; // or set a specific year
        int month = 0; // or set a specific month
        String invoiceAmountCondition = "=";
        BigDecimal invoiceAmount = BigDecimal.valueOf(200.00);

        // Mock the repository response
        when(invoiceRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(invoices));

        // Execute the service method
        List<InvoiceDTO> foundInvoices = invoiceService.getInvoicesByCriteria(customerId, customerName, year, month, invoiceAmountCondition, invoiceAmount, 0, 10);

        // Assert the results
        assertNotNull(foundInvoices);
        assertEquals(1, foundInvoices.size());
        assertEquals("I001", foundInvoices.get(0).getId());
        assertEquals("C001", foundInvoices.get(0).getCustomer().getId());
        assertEquals(BigDecimal.valueOf(200.00), foundInvoices.get(0).getInvoiceAmount());
    }

    @Test
    void testGetInvoiceByCriteriaNoResults() {
        // Set up the search criteria
        String customerId = "C999";
        String customerName = null; // or set a value if needed
        int year = 0; // or set a specific year
        int month = 0; // or set a specific month
        String invoiceAmountCondition = "=";
        BigDecimal invoiceAmount = BigDecimal.valueOf(1000.00);

        // Mock the repository response
        when(invoiceRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(Page.empty());

        // Execute the service method
        List<InvoiceDTO> foundInvoices = invoiceService.getInvoicesByCriteria(customerId, customerName, year, month, invoiceAmountCondition, invoiceAmount, 0, 10);

        // Assert the results
        assertNotNull(foundInvoices);
        assertTrue(foundInvoices.isEmpty());
    }


}
