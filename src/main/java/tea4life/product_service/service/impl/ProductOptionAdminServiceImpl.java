package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.request.CreateProductOptionRequest;
import tea4life.product_service.dto.request.CreateProductOptionValueRequest;
import tea4life.product_service.dto.response.ProductOptionResponse;
import tea4life.product_service.dto.response.ProductOptionValueResponse;
import tea4life.product_service.model.ProductOption;
import tea4life.product_service.model.ProductOptionValue;
import tea4life.product_service.repository.ProductOptionRepository;
import tea4life.product_service.repository.ProductOptionValueRepository;
import tea4life.product_service.service.ProductOptionAdminService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductOptionAdminServiceImpl implements ProductOptionAdminService {

    ProductOptionRepository productOptionRepository;
    ProductOptionValueRepository productOptionValueRepository;

    @Override
    public ProductOptionResponse createOption(CreateProductOptionRequest request) {
        ProductOption option = new ProductOption();
        applyRequestToOption(option, request);
        option = productOptionRepository.save(option);
        return toResponse(option);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductOptionResponse> findAllOptions() {
        return productOptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductOptionResponse findOptionById(Long id) {
        return toResponse(findOptionByIdInternal(id));
    }

    @Override
    public ProductOptionResponse updateOption(Long id, CreateProductOptionRequest request) {
        ProductOption option = findOptionByIdInternal(id);
        applyRequestToOption(option, request);
        option = productOptionRepository.save(option);
        return toResponse(option);
    }

    @Override
    public void deleteOption(Long id) {
        ProductOption option = findOptionByIdInternal(id);
        productOptionRepository.delete(option);
    }

    private ProductOption findOptionByIdInternal(Long id) {
        return productOptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy product option"));
    }

    private void applyRequestToOption(ProductOption option, CreateProductOptionRequest request) {
        option.setName(request.name());
        option.setRequired(request.isRequired());
        option.setMultiSelect(request.isMultiSelect());
        option.setSortOrder(request.sortOrder());
        if (option.getId() != null) {
            productOptionValueRepository.deleteAllByProductOptionId(option.getId());
        }
        option.setProductOptionValues(buildProductOptionValues(option, request.productOptionValues()));
    }

    private ProductOptionResponse toResponse(ProductOption option) {
        List<ProductOptionValueResponse> productOptionValues = option.getProductOptionValues() == null
                ? List.of()
                : option.getProductOptionValues().stream()
                .map(this::toValueResponse)
                .toList();

        return new ProductOptionResponse(
                option.getId() == null ? null : option.getId().toString(),
                option.getName(),
                option.isRequired(),
                option.isMultiSelect(),
                option.getSortOrder(),
                productOptionValues
        );
    }

    private List<ProductOptionValue> buildProductOptionValues(
            ProductOption option,
            List<CreateProductOptionValueRequest> requests
    ) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .map(request -> {
                    ProductOptionValue value = new ProductOptionValue();
                    value.setProductOption(option);
                    value.setValueName(request.valueName());
                    value.setExtraPrice(request.extraPrice());
                    value.setSortOrder(request.sortOrder());
                    return value;
                })
                .toList();
    }

    private ProductOptionValueResponse toValueResponse(ProductOptionValue value) {
        return new ProductOptionValueResponse(
                value.getId() == null ? null : value.getId().toString(),
                value.getProductOption() == null ? null : value.getProductOption().getId().toString(),
                value.getValueName(),
                value.getExtraPrice(),
                value.getSortOrder()
        );
    }
}
