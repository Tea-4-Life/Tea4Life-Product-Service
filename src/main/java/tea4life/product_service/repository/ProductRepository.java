package tea4life.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tea4life.product_service.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
