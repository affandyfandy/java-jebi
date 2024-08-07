package com.fpt.MidtermG1.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import com.fpt.MidtermG1.common.Status;
import com.fpt.MidtermG1.data.entity.Product;

import com.fpt.MidtermG1.data.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveAndFindProduct() {
        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();

        product = productRepository.save(product);

        Product foundProduct = productRepository.findById(product.getId()).orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void testFindAllProducts() {
        Product product1 = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();

        Product product2 = Product.builder()
                .name("Smartphone")
                .price(new BigDecimal("800"))
                .status(Status.INACTIVE)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }

    @Test
    void testFindByNameContaining() {
        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .status(Status.ACTIVE)
                .build();

        productRepository.save(product);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productsPage = productRepository.findByNameContaining("Lap", pageable);
        assertThat(productsPage.getTotalElements()).isEqualTo(1);
        assertThat(productsPage.getContent().get(0).getName()).isEqualTo("Laptop");
    }
}
