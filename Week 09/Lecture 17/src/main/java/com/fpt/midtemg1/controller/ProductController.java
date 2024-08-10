package com.fpt.midtemg1.controller;

import com.fpt.midtemg1.dto.ProductDTO;
import com.fpt.midtemg1.exception.ProductImportException;
import com.fpt.midtemg1.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductDTO> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String search,
                                           @RequestParam(defaultValue = "name") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.listAllProduct(pageable, search);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        ProductDTO savedProduct = productService.saveProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable int id, @RequestBody @Valid ProductDTO productDTO) {
        Optional<ProductDTO> updatedProduct = productService.updateProduct(id, productDTO);
        return updatedProduct.map(productDTO1 -> new ResponseEntity<>(productDTO1, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            productService.importExcel(file.getInputStream());
            return new ResponseEntity<>("Products imported successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to import products", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new ProductImportException("Error importing products", e);
        }
    }

    @PutMapping(value = "/activate/{id}")
    public ResponseEntity<String> activateStatus(@PathVariable int id) {
        try {
            productService.activateProduct(id);
            return ResponseEntity.ok("Product activated successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product already active")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already active");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PutMapping(value = "/deactivate/{id}")
    public ResponseEntity<String> deactivateStatus(@PathVariable int id) {
        try {
            productService.deactivateProduct(id);
            return ResponseEntity.ok("Product deactivated successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product already inactive")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Product already inactive");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
}
