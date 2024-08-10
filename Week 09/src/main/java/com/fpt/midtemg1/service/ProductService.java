package com.fpt.midtemg1.service;

import com.fpt.midtemg1.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.Optional;

public interface ProductService {
    Page<ProductDTO> listAllProduct(Pageable pageable, String search);
    Optional<ProductDTO> findProductById(int id);
    ProductDTO saveProduct(ProductDTO productDTO);
    ProductDTO activateProduct(int id);
    ProductDTO deactivateProduct(int id);
    Optional<ProductDTO> updateProduct(int id, ProductDTO productDTO);
    void importExcel(InputStream inputStream) throws Exception;
}
