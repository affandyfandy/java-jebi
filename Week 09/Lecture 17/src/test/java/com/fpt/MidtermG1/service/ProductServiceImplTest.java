package com.fpt.MidtermG1.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Product;
import com.fpt.MidtermG1.data.repository.ProductRepository;
import com.fpt.MidtermG1.dto.ProductDTO;
import com.fpt.MidtermG1.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(BigDecimal.valueOf(100.0));
        productDTO.setStatus(Status.ACTIVE);
        productDTO.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        productDTO.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testSaveProduct() {
        Product product = productDTO.toEntity();
        product.setId(1);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO savedProductDTO = productService.saveProduct(productDTO);

        assertEquals(1, savedProductDTO.getId());
        assertEquals("Test Product", savedProductDTO.getName());
        assertEquals(BigDecimal.valueOf(100.0), savedProductDTO.getPrice());
        assertEquals(Status.ACTIVE, savedProductDTO.getStatus());

        verify(productRepository, times(1)).save(any(Product.class));
    }
}
