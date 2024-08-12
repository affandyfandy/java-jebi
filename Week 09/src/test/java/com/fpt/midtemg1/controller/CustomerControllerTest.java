package com.fpt.midtemg1.controller;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.dto.CustomerDTO;
import com.fpt.midtemg1.service.CustomerService;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO customerDTO;

    private static final String BASE_URL = "/api/v1/customers";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = CustomerDTO.builder()
                .id("C001")
                .name("Jebi Hendardi")
                .phoneNumber("+623456789")
                .status(Status.ACTIVE)
                .build();
    }

    private String customerJson() {
        return "{\"id\":\"C001\",\"name\":\"Jebi Hendardi\",\"phoneNumber\":\"+623456789\",\"status\":\"ACTIVE\"}";
    }

    // Test: Get Customer List
    @Test
    void testGetCustomerList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerDTO> customerPage = new PageImpl<>(Arrays.asList(customerDTO));
        when(customerService.getCustomerList(pageable)).thenReturn(customerPage);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value("C001"))
                .andExpect(jsonPath("$.content[0].name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.content[0].phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Get Customer By ID
    @Test
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById("C001")).thenReturn(Optional.of(customerDTO));

        mockMvc.perform(get(BASE_URL + "/C001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("C001"))
                .andExpect(jsonPath("$.name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Add Customer
    @Test
    void testAddCustomer() throws Exception {
        when(customerService.addCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("C001"))
                .andExpect(jsonPath("$.name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Edit Customer
    @Test
    void testEditCustomer() throws Exception {
        when(customerService.editCustomer(eq("C001"), any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(put(BASE_URL + "/C001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("C001"))
                .andExpect(jsonPath("$.name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Activate Customer
    @Test
    void testActivateCustomer() throws Exception {
        when(customerService.activateCustomer("C001")).thenReturn(customerDTO);

        mockMvc.perform(put(BASE_URL + "/activate/C001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("C001"))
                .andExpect(jsonPath("$.name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Deactivate Customer
    @Test
    void testDeactivateCustomer() throws Exception {
        CustomerDTO deactivatedCustomerDTO = CustomerDTO.builder()
                .id("C001")
                .name("Jebi Hendardi")
                .phoneNumber("+623456789")
                .status(Status.INACTIVE)  // Set the status to INACTIVE
                .build();

        when(customerService.deactivateCustomer("C001")).thenReturn(deactivatedCustomerDTO);

        mockMvc.perform(put(BASE_URL + "/deactivate/C001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("C001"))
                .andExpect(jsonPath("$.name").value("Jebi Hendardi"))
                .andExpect(jsonPath("$.phoneNumber").value("+623456789"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andDo(print());
    }

    // Test: Get Customer By ID Not Found
    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        when(customerService.getCustomerById("C001")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/C001"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    // Test: Get Customer List Empty
    @Test
    void testGetCustomerListEmpty() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerDTO> customerPage = new PageImpl<>(Arrays.asList()); // Empty list
        when(customerService.getCustomerList(pageable)).thenReturn(customerPage);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    // Test: Activate Customer Already Active
    @Test
    void testActivateCustomerAlreadyActive() throws Exception {
        CustomerDTO alreadyActiveCustomerDTO = CustomerDTO.builder()
                .id("C001")
                .name("Jebi Hendardi")
                .phoneNumber("+623456789")
                .status(Status.ACTIVE)  // Already active
                .build();

        when(customerService.activateCustomer("C001")).thenReturn(alreadyActiveCustomerDTO);

        mockMvc.perform(put(BASE_URL + "/activate/C001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    // Test: Deactivate Customer Already Inactive
    @Test
    void testDeactivateCustomerAlreadyInactive() throws Exception {
        CustomerDTO alreadyInactiveCustomerDTO = CustomerDTO.builder()
                .id("C001")
                .name("Jebi Hendardi")
                .phoneNumber("+623456789")
                .status(Status.INACTIVE)  // Already inactive
                .build();

        when(customerService.deactivateCustomer("C001")).thenReturn(alreadyInactiveCustomerDTO);

        mockMvc.perform(put(BASE_URL + "/deactivate/C001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andDo(print());
    }

    // Test: Add Customer Internal Error
    @Test
    void testAddCustomerInternalError() throws Exception {
        when(customerService.addCustomer(any(CustomerDTO.class))).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson()))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    // Test: Search Customers Empty Result
    @Test
    void testSearchCustomersEmptyResult() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerDTO> emptyCustomerPage = new PageImpl<>(Arrays.asList()); // Empty result
        when(customerService.searchCustomers(eq("non-existent-keyword"), eq(pageable))).thenReturn(emptyCustomerPage);

        mockMvc.perform(get(BASE_URL).param("keyword", "non-existent-keyword"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    // Test: Edit Customer Internal Error
    @Test
    void testEditCustomerInternalError() throws Exception {
        when(customerService.editCustomer(eq("C001"), any(CustomerDTO.class))).thenThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(put(BASE_URL + "/C001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson()))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}
