package tea4life.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tea4life.product_service.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"productCategory", "productOptions"})
    Page<Product> findAllBy(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"productCategory", "productOptions"})
    Optional<Product> findById(Long id);

    @EntityGraph(attributePaths = {"productCategory", "productOptions", "productOptions.productOptionValues"})
    Optional<Product> findDetailById(Long id);
}
