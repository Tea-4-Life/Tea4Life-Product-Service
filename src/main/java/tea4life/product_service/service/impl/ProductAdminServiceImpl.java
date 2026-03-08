package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.client.StorageClient;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.request.CreateProductRequest;
import tea4life.product_service.dto.request.FileMoveRequest;
import tea4life.product_service.dto.response.ProductResponse;
import tea4life.product_service.model.Product;
import tea4life.product_service.model.ProductCategory;
import tea4life.product_service.model.ProductOption;
import tea4life.product_service.repository.ProductCategoryRepository;
import tea4life.product_service.repository.ProductOptionRepository;
import tea4life.product_service.repository.ProductRepository;
import tea4life.product_service.service.ProductAdminService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductAdminServiceImpl implements ProductAdminService {

    ProductRepository productRepository;
    ProductCategoryRepository productCategoryRepository;
    ProductOptionRepository productOptionRepository;
    StorageClient storageClient;
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.storage-delete-file}")
    @NonFinal
    String storageDeleteFileTopic;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = new Product();
        applyRequestToProduct(product, request);
        product = productRepository.save(product);

        if (hasText(request.imageKey())) {
            String destinationPath = "products/items/" + product.getId();
            ApiResponse<String> storageResponse = storageClient.confirmFile(new FileMoveRequest(request.imageKey(), destinationPath));
            if (storageResponse.getErrorCode() != null) {
                throw new RuntimeException("Loi di chuyen file: " + storageResponse.getErrorMessage());
            }
            product.setImageUrl(storageResponse.getData());
            product = productRepository.save(product);
        }

        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> findAllProducts(Pageable pageable) {
        Page<ProductResponse> responsePage = productRepository.findAllBy(pageable)
                .map(this::toResponse);
        return new PageResponse<>(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long id) {
        return toResponse(findProductEntityById(id));
    }

    @Override
    public ProductResponse updateProduct(Long id, CreateProductRequest request) {
        Product product = findProductEntityById(id);
        String oldImageUrl = product.getImageUrl();

        applyRequestToProduct(product, request);

        if (hasText(request.imageKey())) {
            String destinationPath = "products/items/" + product.getId();
            ApiResponse<String> storageResponse = storageClient.confirmFile(new FileMoveRequest(request.imageKey(), destinationPath));
            if (storageResponse.getErrorCode() != null) {
                throw new RuntimeException("Loi di chuyen file: " + storageResponse.getErrorMessage());
            }
            product.setImageUrl(storageResponse.getData());
        }

        Product saved = productRepository.save(product);
        if (hasText(request.imageKey()) && !Objects.equals(oldImageUrl, saved.getImageUrl())) {
            publishStorageDelete(oldImageUrl);
        }
        return toResponse(saved);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findProductEntityById(id);
        String imageUrl = product.getImageUrl();
        productRepository.delete(product);
        publishStorageDelete(imageUrl);
    }

    private void applyRequestToProduct(Product product, CreateProductRequest request) {
        product.setProductCategory(findCategoryById(parseRequiredId(request.productCategoryId(), "productCategoryId")));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBasePrice(request.basePrice());
        product.setProductOptions(resolveProductOptions(request.productOptionIds()));
    }

    private Product findProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay san pham"));
    }

    private ProductCategory findCategoryById(Long categoryId) {
        return productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay danh muc san pham"));
    }

    private List<ProductOption> resolveProductOptions(List<String> productOptionIds) {
        if (productOptionIds == null || productOptionIds.isEmpty()) {
            return List.of();
        }

        List<Long> optionIds = productOptionIds.stream()
                .map(id -> parseRequiredId(id, "productOptionId"))
                .toList();

        List<ProductOption> options = productOptionRepository.findAllById(optionIds);
        Set<Long> foundIds = options.stream().map(ProductOption::getId).collect(java.util.stream.Collectors.toSet());

        for (Long optionId : new HashSet<>(optionIds)) {
            if (!foundIds.contains(optionId)) {
                throw new EntityNotFoundException("Khong tim thay product option: " + optionId);
            }
        }

        return options;
    }

    private Long parseRequiredId(String rawId, String fieldName) {
        if (!hasText(rawId)) {
            throw new IllegalArgumentException(fieldName + " khong duoc de trong");
        }
        try {
            return Long.parseLong(rawId.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " khong hop le", ex);
        }
    }

    private ProductResponse toResponse(Product product) {
        List<String> productOptionIds = product.getProductOptions() == null
                ? List.of()
                : product.getProductOptions().stream().map(option -> option.getId().toString()).toList();

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

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void publishStorageDelete(String fileUrl) {
        if (hasText(fileUrl)) {
            kafkaTemplate.send(storageDeleteFileTopic, fileUrl);
        }
    }
}
