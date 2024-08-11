package com.fpt.midtemg1.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.midtemg1.dto.InvoiceDTO;
import com.fpt.midtemg1.dto.RevenueReportDTO;
import com.fpt.midtemg1.service.InvoiceService;


class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
    }

    @Test
    void testAddInvoice() throws Exception {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceService.addInvoice(any(InvoiceDTO.class))).thenReturn(invoiceDTO);

        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invoiceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testEditInvoice() throws Exception {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceService.editInvoice(anyString(), any(InvoiceDTO.class))).thenReturn(invoiceDTO);

        mockMvc.perform(put("/api/v1/invoices/{id}", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invoiceDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetInvoiceById() throws Exception {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceService.getInvoiceById(anyString())).thenReturn(invoiceDTO);

        mockMvc.perform(get("/api/v1/invoices/{id}", "123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllInvoices() throws Exception {
        List<InvoiceDTO> invoices = Collections.singletonList(new InvoiceDTO());
        when(invoiceService.getAllInvoices(anyInt(), anyInt())).thenReturn(invoices);

        mockMvc.perform(get("/api/v1/invoices")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetInvoicesByCriteria() throws Exception {
        List<InvoiceDTO> invoices = Collections.singletonList(new InvoiceDTO());
        when(invoiceService.getInvoicesByCriteria(anyString(), anyString(), anyInt(), anyInt(), anyString(), any(BigDecimal.class), anyInt(), anyInt()))
                .thenReturn(invoices);

        mockMvc.perform(get("/api/v1/invoices/search")
                        .param("customerId", "1")
                        .param("customerName", "John")
                        .param("year", "2024")
                        .param("month", "8")
                        .param("invoiceAmountCondition", ">")
                        .param("invoiceAmount", "100.00")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testExportToPDF() throws Exception {
        when(invoiceService.exportAllInvoicesToPDF()).thenReturn(new byte[0]);

        mockMvc.perform(get("/api/v1/invoices/export-pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN));
    }

    @Test
    void testGetRevenueReport() throws Exception {
        List<RevenueReportDTO> reports = Collections.singletonList(new RevenueReportDTO());
        when(invoiceService.getRevenueByPeriod(anyInt(), anyInt(), anyInt())).thenReturn(reports);

        mockMvc.perform(get("/api/v1/invoices/report")
                        .param("year", "2024")
                        .param("month", "8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testExportToPDFException() throws Exception {
        when(invoiceService.exportAllInvoicesToPDF()).thenThrow(new RuntimeException("PDF export error"));

        mockMvc.perform(get("/api/v1/invoices/export-pdf"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(content().string("Error saving file: PDF export error"));
    }


    @Test
    void testGetAllInvoicesEmpty() throws Exception {
        when(invoiceService.getAllInvoices(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/invoices")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetRevenueReportEmpty() throws Exception {
        when(invoiceService.getRevenueByPeriod(anyInt(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/invoices/report"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

}
