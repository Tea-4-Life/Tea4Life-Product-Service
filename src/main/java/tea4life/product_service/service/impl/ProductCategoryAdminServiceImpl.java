package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.client.StorageClient;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.request.CreateProductCategoryRequest;
import tea4life.product_service.dto.request.FileMoveRequest;
import tea4life.product_service.dto.response.ProductCategoryResponse;
import tea4life.product_service.model.ProductCategory;
import tea4life.product_service.repository.ProductCategoryRepository;
import tea4life.product_service.service.ProductCategoryAdminService;

import java.util.List;

/**
 * Admin 2/25/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductCategoryAdminServiceImpl implements ProductCategoryAdminService {

    ProductCategoryRepository productCategoryRepository;
    StorageClient storageClient;

    @Override
    public ProductCategoryResponse createCategory(CreateProductCategoryRequest request) {
        if (productCategoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DataIntegrityViolationException("Tên danh mục đã tồn tại");
        }

        ProductCategory category = new ProductCategory();
        applyRequestToCategory(category, request);
        category = productCategoryRepository.save(category);

        if (hasText(request.iconKey())) {
            String destinationPath = "products/categories/" + category.getId();
            ApiResponse<String> storageResponse = storageClient.confirmFile(
                    new FileMoveRequest(request.iconKey(), destinationPath)
            );
            if (storageResponse.getErrorCode() != null) {
                throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
            }
            category.setIconUrl(storageResponse.getData());
            category = productCategoryRepository.save(category);
        }

        return toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> findAllCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryResponse findCategoryById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public ProductCategoryResponse updateCategory(Long id, CreateProductCategoryRequest request) {
        ProductCategory category = findById(id);

        if (!category.getName().equalsIgnoreCase(request.name())
                && productCategoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DataIntegrityViolationException("Tên danh mục đã tồn tại");
        }

        applyRequestToCategory(category, request);
        if (hasText(request.iconKey())) {
            String destinationPath = "products/categories/" + category.getId();
            ApiResponse<String> storageResponse = storageClient.confirmFile(
                    new FileMoveRequest(request.iconKey(), destinationPath)
            );
            if (storageResponse.getErrorCode() != null) {
                throw new RuntimeException("Lỗi di chuyển file: " + storageResponse.getErrorMessage());
            }
            category.setIconUrl(storageResponse.getData());
        }
        return toResponse(productCategoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        ProductCategory category = findById(id);
        productCategoryRepository.delete(category);
    }

    private ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục sản phẩm"));
    }

    private void applyRequestToCategory(ProductCategory category, CreateProductCategoryRequest request) {
        category.setName(request.name());
        category.setDescription(request.description());
    }

    private ProductCategoryResponse toResponse(ProductCategory category) {
        return ProductCategoryResponse.builder()
                .id(category.getId() == null ? null : category.getId().toString())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .build();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
