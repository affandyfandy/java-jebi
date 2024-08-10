package com.fpt.midtemg1.controller;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.dto.ProductDTO;
import com.fpt.midtemg1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDTO productDTO;

    private static final String BASE_URL = "/api/v1/products";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productDTO = ProductDTO.builder()
                .id(1)
                .name("Sample Product")
                .price(BigDecimal.valueOf(100.0))
                .status(Status.ACTIVE)
                .build();
    }

    private String productJson() {
        return "{\"id\":1,\"name\":\"Sample Product\",\"price\":100.0,\"status\":\"ACTIVE\"}";
    }

    @Test
    public void testGetAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<ProductDTO> productPage = new PageImpl<>(Arrays.asList(productDTO), pageable, 1);
        when(productService.listAllProduct(pageable, null)).thenReturn(productPage);

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) // Ensure content type is set
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Sample Product"))
                .andExpect(jsonPath("$.content[0].price").value(100.0))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))
                .andDo(print());
    }

    @Test
    public void testAddProduct() throws Exception {
        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Product"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    @Test
    public void testEditProduct() throws Exception {
        when(productService.updateProduct(eq(1), any(ProductDTO.class))).thenReturn(Optional.of(productDTO));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sample Product"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andDo(print());
    }

    @Test
    public void testEditProductNotFound() throws Exception {
        when(productService.updateProduct(eq(1), any(ProductDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testImportProducts() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", new ByteArrayInputStream("test".getBytes()));
        doNothing().when(productService).importExcel(file.getInputStream());

        mockMvc.perform(multipart(BASE_URL + "/import")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Products imported successfully"))
                .andDo(print());
    }

    @Test
    public void testActivateProduct() throws Exception {
        when(productService.activateProduct(1)).thenReturn(productDTO);

        mockMvc.perform(put(BASE_URL + "/activate/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Product activated successfully"))
                .andDo(print());
    }


    @Test
    public void testActivateProductNotFound() throws Exception {
        when(productService.activateProduct(1)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put(BASE_URL + "/activate/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"))
                .andDo(print());
    }

    @Test
    public void testActivateProductAlreadyActive() throws Exception {
        // Product is already active
        when(productService.activateProduct(1)).thenThrow(new RuntimeException("Product already active"));

        mockMvc.perform(put(BASE_URL + "/activate/1"))
                .andExpect(status().isConflict()) // Use 409 Conflict to indicate the resource is already in the requested state
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Product already active"))
                .andDo(print());
    }


    @Test
    public void testDeactivateProduct() throws Exception {
        // Product is being deactivated
        ProductDTO deactivatedProductDTO = ProductDTO.builder()
                .id(1)
                .name("Sample Product")
                .price(BigDecimal.valueOf(100.0))
                .status(Status.INACTIVE)
                .build();

        when(productService.deactivateProduct(1)).thenReturn(deactivatedProductDTO);

        mockMvc.perform(put(BASE_URL + "/deactivate/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Product deactivated successfully"))
                .andDo(print());
    }


    @Test
    public void testDeactivateProductNotFound() throws Exception {
        // Set up the ProductService to throw an exception for not found
        when(productService.deactivateProduct(1)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put(BASE_URL + "/deactivate/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"))
                .andDo(print());
    }


    @Test
    public void testDeactivateProductAlreadyInactive() throws Exception {
        // Product is already inactive
        when(productService.deactivateProduct(1)).thenThrow(new RuntimeException("Product already inactive"));

        mockMvc.perform(put(BASE_URL + "/deactivate/1"))
                .andExpect(status().isConflict()) // Use 409 Conflict to indicate the resource is already in the requested state
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Product already inactive"))
                .andDo(print());
    }

    @Test
    public void testAddProductValidationError() throws Exception {
        String invalidProductJson = "{\"id\":1,\"name\":\"\",\"price\":100.0,\"status\":\"ACTIVE\"}"; // Name is blank

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testEditProductValidationError() throws Exception {
        String invalidProductJson = "{\"id\":1,\"name\":\"\",\"price\":100.0,\"status\":\"ACTIVE\"}"; // Name is blank

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testImportProductsIOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", new byte[0]);

        doThrow(new IOException("Test IOException")).when(productService).importExcel(any());

        mockMvc.perform(multipart(BASE_URL + "/import").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to import products"))
                .andDo(print());
    }

    @Test
    public void testImportProductsRuntimeException() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", new byte[0]);

        doThrow(new RuntimeException("Test RuntimeException")).when(productService).importExcel(any());

        mockMvc.perform(multipart(BASE_URL + "/import").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Error importing products"))
                .andDo(print());
    }


    @Test
    public void testActivateProductUnexpectedException() throws Exception {
        when(productService.activateProduct(1)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put(BASE_URL + "/activate/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"))
                .andDo(print());
    }


    @Test
    public void testDeactivateProductUnexpectedException() throws Exception {
        when(productService.deactivateProduct(1)).thenThrow(new RuntimeException("Product not found"));

        mockMvc.perform(put(BASE_URL + "/deactivate/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"))
                .andDo(print());
    }







}
