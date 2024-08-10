package com.fpt.midtemg1.service.impl;

import com.fpt.midtemg1.common.Status;
import com.fpt.midtemg1.data.entity.Product;
import com.fpt.midtemg1.data.repository.ProductRepository;
import com.fpt.midtemg1.dto.InvoiceProductDTO;
import com.fpt.midtemg1.dto.ProductDTO;
import com.fpt.midtemg1.exception.ProductStatusException;
import com.fpt.midtemg1.exception.ResourceNotFoundException;
import com.fpt.midtemg1.service.ProductService;
import com.fpt.midtemg1.specifications.ProductSpecificationsBuilder;
import com.fpt.midtemg1.util.ExcelUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Page<ProductDTO> listAllProduct(Pageable pageable, String search) {
        if (search.length() > 1000) {
            throw new IllegalArgumentException("Search query is too long.");
        }

        String sanitizedSearch = search.trim();
        ProductSpecificationsBuilder builder = new ProductSpecificationsBuilder();

        String[] criteria = sanitizedSearch.split(",");
        for (String criterion : criteria) {
            String[] parts = criterion.split("[:<>]", 2); // Split into two parts: key and value
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                char operation = criterion.charAt(parts[0].length());
                builder.with(key, String.valueOf(operation), value);
            }
        }

        Specification<Product> spec = builder.build();
        Page<Product> products = productRepository.findAll(spec, pageable);

        return products.map(Product::toDTO);
    }




    @Override
    public Optional<ProductDTO> findProductById(int id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found for this id : " + id);
        }
        return productOpt.map(Product::toDTO);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product savedProduct = productRepository.save(productDTO.toEntity());
        return savedProduct.toDTO();
    }

    @Override
    public Optional<ProductDTO> updateProduct(int id, ProductDTO productDTO) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found for this id : " + id);
        } else {
            Product product = productOpt.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setStatus(productDTO.getStatus());
            product.setCreatedTime(productDTO.getCreatedTime());
            product.setUpdatedTime(productDTO.getUpdatedTime());

            if (productDTO.getInvoiceProducts() != null) {
                product.setInvoiceProducts(productDTO.getInvoiceProducts().stream()
                        .map(InvoiceProductDTO::toEntity)
                        .collect(Collectors.toSet()));
            }

            Product updatedProduct = productRepository.save(product);
            return Optional.of(updatedProduct.toDTO());
        }
    }

    @Override
    public void importExcel(InputStream inputStream) throws Exception {
        if (inputStream.available() == 0) {
            return;
        }

        List<Product> products = ExcelUtil.parseProductFile(inputStream);
        productRepository.saveAll(products);
    }


    @Override
    public ProductDTO activateProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getStatus() == Status.INACTIVE) {
            product.setStatus(Status.ACTIVE);
        } else {
            throw new ProductStatusException("Product status already " + product.getStatus());
        }

        return productRepository.save(product).toDTO();
    }

    @Override
    public ProductDTO deactivateProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getStatus() == Status.ACTIVE) {
            product.setStatus(Status.INACTIVE);
        } else {
            throw new ProductStatusException("Product status already " + product.getStatus());
        }

        return productRepository.save(product).toDTO();
    }

}
