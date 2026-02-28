package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.response.ProductResponse;
import tea4life.product_service.dto.response.ProductSummaryResponse;
import tea4life.product_service.model.Product;
import tea4life.product_service.repository.ProductRepository;
import tea4life.product_service.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> findProducts(Pageable pageable) {
        Page<@NonNull ProductSummaryResponse> responsePage = productRepository.findAllBy(pageable)
                .map(this::toSummaryResponse);

        return new PageResponse<>(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));
        return toDetailResponse(product);
    }

    private ProductSummaryResponse toSummaryResponse(Product product) {
        return new ProductSummaryResponse(
                product.getId().toString(),
                product.getName(),
                product.getBasePrice(),
                product.getImageUrl(),
                product.getProductCategory().getName()
        );
    }

    private ProductResponse toDetailResponse(Product product) {
        List<String> productOptionIds = product.getProductOptions() == null
                ? List.of()
                : product.getProductOptions().stream()
                .map(productOption -> productOption.getId().toString())
                .toList();

        return new ProductResponse(
                product.getId().toString(),
                product.getProductCategory().getId().toString(),
                product.getProductCategory().getName(),
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getImageUrl(),
                productOptionIds
        );
    }
}
