package tea4life.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tea4life.product_service.model.ProductOptionValue;

import java.util.List;
import java.util.Optional;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
    List<ProductOptionValue> findAllByProductOptionIdOrderBySortOrderAsc(Long productOptionId);

    Optional<ProductOptionValue> findByIdAndProductOptionId(Long id, Long productOptionId);
}
