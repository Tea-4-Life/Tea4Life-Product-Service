package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.request.CreateProductOptionValueRequest;
import tea4life.product_service.dto.response.ProductOptionValueResponse;
import tea4life.product_service.model.ProductOption;
import tea4life.product_service.model.ProductOptionValue;
import tea4life.product_service.repository.ProductOptionRepository;
import tea4life.product_service.repository.ProductOptionValueRepository;
import tea4life.product_service.service.ProductOptionValueAdminService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductOptionValueAdminServiceImpl implements ProductOptionValueAdminService {

    ProductOptionRepository productOptionRepository;
    ProductOptionValueRepository productOptionValueRepository;

    @Override
    public ProductOptionValueResponse createValue(Long productOptionId, CreateProductOptionValueRequest request) {
        ProductOption option = findOptionById(productOptionId);

        ProductOptionValue value = new ProductOptionValue();
        value.setProductOption(option);
        applyRequestToValue(value, request);

        return toResponse(productOptionValueRepository.save(value));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionValueResponse> findAllValues(Long productOptionId) {
        ensureOptionExists(productOptionId);
        return productOptionValueRepository.findAllByProductOptionIdOrderBySortOrderAsc(productOptionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOptionValueResponse findValueById(Long productOptionId, Long id) {
        ensureOptionExists(productOptionId);
        return toResponse(findValueEntityById(productOptionId, id));
    }

    @Override
    public ProductOptionValueResponse updateValue(Long productOptionId, Long id, CreateProductOptionValueRequest request) {
        ensureOptionExists(productOptionId);
        ProductOptionValue value = findValueEntityById(productOptionId, id);
        applyRequestToValue(value, request);
        return toResponse(productOptionValueRepository.save(value));
    }

    @Override
    public void deleteValue(Long productOptionId, Long id) {
        ensureOptionExists(productOptionId);
        ProductOptionValue value = findValueEntityById(productOptionId, id);
        productOptionValueRepository.delete(value);
    }

    private ProductOption findOptionById(Long productOptionId) {
        return productOptionRepository.findById(productOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy product option"));
    }

    private void ensureOptionExists(Long productOptionId) {
        if (!productOptionRepository.existsById(productOptionId)) {
            throw new EntityNotFoundException("Không tìm thấy product option");
        }
    }

    private ProductOptionValue findValueEntityById(Long productOptionId, Long id) {
        return productOptionValueRepository.findByIdAndProductOptionId(id, productOptionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy product option value"));
    }

    private void applyRequestToValue(ProductOptionValue value, CreateProductOptionValueRequest request) {
        value.setValueName(request.valueName());
        value.setExtraPrice(request.extraPrice());
        value.setSortOrder(request.sortOrder());
    }

    private ProductOptionValueResponse toResponse(ProductOptionValue value) {
        return new ProductOptionValueResponse(
                value.getId() == null ? null : value.getId().toString(),
                value.getProductOption() == null ? null : value.getProductOption().getId().toString(),
                value.getValueName(),
                value.getExtraPrice(),
                value.getSortOrder()
        );
    }
}
