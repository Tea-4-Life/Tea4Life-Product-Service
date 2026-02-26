package tea4life.product_service.service;

import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.request.CreateProductOptionValueRequest;
import tea4life.product_service.dto.response.ProductOptionValueResponse;

import java.util.List;

public interface ProductOptionValueAdminService {
    ProductOptionValueResponse createValue(Long productOptionId, CreateProductOptionValueRequest request);

    @Transactional(readOnly = true)
    List<ProductOptionValueResponse> findAllValues(Long productOptionId);

    @Transactional(readOnly = true)
    ProductOptionValueResponse findValueById(Long productOptionId, Long id);

    ProductOptionValueResponse updateValue(Long productOptionId, Long id, CreateProductOptionValueRequest request);

    void deleteValue(Long productOptionId, Long id);
}
