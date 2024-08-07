package com.fpt.MidtermG1.service;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Product;
import com.fpt.MidtermG1.data.repository.ProductRepository;
import com.fpt.MidtermG1.dto.ProductDTO;
import com.fpt.MidtermG1.exception.ResourceNotFoundException;
import com.fpt.MidtermG1.service.impl.ProductServiceImpl;
import com.fpt.MidtermG1.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

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

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
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
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAll((Specification<Product>) any(), eq(pageable))).thenReturn(productPage);

        Page<ProductDTO> result = productService.listAllProduct(pageable, "name:Test");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll((Specification<Product>) any(), eq(pageable));
    }


    @Test
    void testFindProductById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.findProductById(1);

        assertTrue(result.isPresent());
        assertEquals(productDTO.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1);
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
        assertEquals(productDTO.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Optional<ProductDTO> result = productService.updateProduct(1, productDTO);

        assertTrue(result.isPresent());
        assertEquals(productDTO.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(1, productDTO));
    }

    @Test
    void testImportExcel() throws Exception {
        // Create a simple Excel workbook with Apache POI
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Products");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Price");
        header.createCell(3).setCellValue("Status");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(1);
        dataRow.createCell(1).setCellValue("Test Product");
        dataRow.createCell(2).setCellValue(100.0);
        dataRow.createCell(3).setCellValue("ACTIVE");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        byte[] excelBytes = bos.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(excelBytes);
        List<Product> products = Collections.singletonList(product);

        // Mock the static method of ExcelUtil class
        try (MockedStatic<ExcelUtil> mockedExcelUtil = mockStatic(ExcelUtil.class)) {
            mockedExcelUtil.when(() -> ExcelUtil.parseProductFile(any(InputStream.class))).thenReturn(products);

            productService.importExcel(inputStream);

            verify(productRepository, times(1)).saveAll(products);
        }
    }


    @Test
    void testActivateProduct() {
        product.setStatus(Status.INACTIVE);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.activateProduct(1);

        assertEquals(Status.ACTIVE, result.getStatus());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testActivateProduct_AlreadyActive() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> productService.activateProduct(1));
    }

    @Test
    void testDeactivateProduct() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.deactivateProduct(1);

        assertEquals(Status.INACTIVE, result.getStatus());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeactivateProduct_AlreadyInactive() {
        product.setStatus(Status.INACTIVE);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> productService.deactivateProduct(1));
    }
}
