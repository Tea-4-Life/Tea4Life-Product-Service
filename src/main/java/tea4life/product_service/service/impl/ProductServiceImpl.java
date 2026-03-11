package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.event.ProductClickedEvent;
import tea4life.product_service.dto.response.ProductCategoryResponse;
import tea4life.product_service.dto.response.ProductDetailResponse;
import tea4life.product_service.dto.response.ProductOptionResponse;
import tea4life.product_service.dto.response.ProductOptionValueResponse;
import tea4life.product_service.dto.response.ProductSummaryResponse;
import tea4life.product_service.model.Product;
import tea4life.product_service.model.ProductCategory;
import tea4life.product_service.model.ProductOption;
import tea4life.product_service.model.ProductOptionValue;
import tea4life.product_service.repository.ProductRepository;
import tea4life.product_service.service.ProductService;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.product-clicked}")
    @NonFinal
    String productClickedTopic;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> findProducts(
            Pageable pageable,
            String keyword,
            Long categoryId,
            Double minPrice,
            Double maxPrice
    ) {
        validatePriceRange(minPrice, maxPrice);

        Page<@NonNull ProductSummaryResponse> responsePage = productRepository
                .findAllByFilters(normalizeKeyword(keyword), categoryId, minPrice, maxPrice, pageable)
                .map(this::toSummaryResponse);

        return new PageResponse<>(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse findProductById(Long id) {
        Product product = productRepository
                .findDetailById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));

        publishProductClicked(product.getId());
        return toDetailResponse(product);
    }

    private void publishProductClicked(Long productId) {
        if (productId == null) return;

        try {
            kafkaTemplate.send(productClickedTopic, new ProductClickedEvent(productId));
        } catch (Exception ex) {
            log.warn("Failed to publish product_clicked event for productId={}: {}", productId, ex.getMessage());
        }

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

    private ProductDetailResponse toDetailResponse(Product product) {
        List<ProductOptionResponse> productOptions = product.getProductOptions() == null
                ? List.of()
                : product.getProductOptions().stream()
                .sorted(Comparator.comparing(ProductOption::getSortOrder))
                .map(this::toOptionResponse)
                .toList();

        return new ProductDetailResponse(
                product.getId().toString(),
                toCategoryResponse(product.getProductCategory()),
                product.getName(),
                product.getDescription(),
                product.getBasePrice(),
                product.getImageUrl(),
                productOptions
        );
    }

    private ProductCategoryResponse toCategoryResponse(ProductCategory category) {
        return ProductCategoryResponse.builder()
                .id(category.getId().toString())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .build();
    }

    private ProductOptionResponse toOptionResponse(ProductOption option) {
        List<ProductOptionValueResponse> productOptionValues = option.getProductOptionValues() == null
                ? List.of()
                : option.getProductOptionValues().stream()
                .sorted(Comparator.comparing(ProductOptionValue::getSortOrder))
                .map(this::toValueResponse)
                .toList();

        return new ProductOptionResponse(
                option.getId().toString(),
                option.getName(),
                option.isRequired(),
                option.isMultiSelect(),
                option.getSortOrder(),
                productOptionValues
        );
    }

    private ProductOptionValueResponse toValueResponse(ProductOptionValue value) {
        return new ProductOptionValueResponse(
                value.getId().toString(),
                value.getProductOption().getId().toString(),
                value.getValueName(),
                value.getExtraPrice(),
                value.getSortOrder(),
                value.getImageUrl()
        );
    }

    private void validatePriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice)
            throw new IllegalArgumentException("minPrice must be less than or equal to maxPrice");
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) return null;

        String trimmedKeyword = keyword.trim();
        return trimmedKeyword.isEmpty() ? null : trimmedKeyword;
    }
}
