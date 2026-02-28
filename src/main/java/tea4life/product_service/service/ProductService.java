package tea4life.product_service.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.response.ProductResponse;
import tea4life.product_service.dto.response.ProductSummaryResponse;

public interface ProductService {
    @Transactional(readOnly = true)
    PageResponse<ProductSummaryResponse> findProducts(Pageable pageable);

    @Transactional(readOnly = true)
    ProductResponse findProductById(Long id);
}
