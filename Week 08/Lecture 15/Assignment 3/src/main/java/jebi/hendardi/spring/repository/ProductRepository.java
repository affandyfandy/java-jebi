package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
