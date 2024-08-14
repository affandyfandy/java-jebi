package jebi.product.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.product.service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
}
