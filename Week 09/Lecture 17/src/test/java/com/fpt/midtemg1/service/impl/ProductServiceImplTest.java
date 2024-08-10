package com.fpt.midtemg1.service.impl;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.data.repository.ProductRepository;
import com.fpt.midtemg1.dto.ProductDTO;
import com.fpt.midtemg1.exception.ProductStatusException;
import com.fpt.midtemg1.exception.ResourceNotFoundException;
import com.fpt.midtemg1.util.ExcelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setStatus(Status.ACTIVE);

        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.valueOf(100.0));
        productDTO.setStatus(Status.ACTIVE);
    }

    @Test
    void testListAllProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = Collections.singletonList(product);
        Page<Product> productPage = new PageImpl<>(products, pageable, 1);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);

        Page<ProductDTO> result = productService.listAllProduct(pageable, "name:Test,");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
    }

    @Test
    void testFindProductById_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.findProductById(1);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
    }

    @Test
    void testFindProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(1));
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.saveProduct(productDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<ProductDTO> result = productService.updateProduct(1, productDTO);

        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1, productDTO));
    }

    @Test
    void testActivateProduct_Success() {
        product.setStatus(Status.INACTIVE);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.activateProduct(1);

        assertNotNull(result);
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    @Test
    void testActivateProduct_AlreadyActive() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThrows(ProductStatusException.class, () -> productService.activateProduct(1));
    }

    @Test
    void testDeactivateProduct_Success() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.deactivateProduct(1);

        assertNotNull(result);
        assertEquals(Status.INACTIVE, result.getStatus());
    }

    @Test
    void testDeactivateProduct_AlreadyInactive() {
        product.setStatus(Status.INACTIVE);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThrows(ProductStatusException.class, () -> productService.deactivateProduct(1));
    }

    @Test
    void testImportExcel() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/test.xlsx");

        assertNotNull(inputStream, "File Not Found");

        productService.importExcel(inputStream);

        verify(productRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testImportExcel_EmptyFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        productService.importExcel(inputStream);

        verify(productRepository, times(0)).saveAll(anyList());
    }


}
