package tea4life.product_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.product_service.dto.request.CreateProductOptionRequest;
import tea4life.product_service.dto.response.ProductOptionResponse;
import tea4life.product_service.model.Product;
import tea4life.product_service.model.ProductOption;
import tea4life.product_service.repository.ProductOptionRepository;
import tea4life.product_service.repository.ProductRepository;
import tea4life.product_service.service.ProductOptionAdminService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProductOptionAdminServiceImpl implements ProductOptionAdminService {

    ProductOptionRepository productOptionRepository;
    ProductRepository productRepository;

    @Override
    public ProductOptionResponse createOption(CreateProductOptionRequest request) {
        ProductOption option = new ProductOption();
        applyRequestToOption(option, request);
        return toResponse(productOptionRepository.save(option));
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
        return toResponse(productOptionRepository.save(option));
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
        option.setProducts(resolveProducts(request.productIds()));
    }

    private List<Product> resolveProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new EntityNotFoundException("Một hoặc nhiều productId không tồn tại");
        }
        return products;
    }

    private ProductOptionResponse toResponse(ProductOption option) {
        List<String> productIds = option.getProducts() == null
                ? List.of()
                : option.getProducts().stream()
                .map(product -> product.getId().toString())
                .toList();

        return new ProductOptionResponse(
                option.getId() == null ? null : option.getId().toString(),
                option.getName(),
                option.isRequired(),
                option.isMultiSelect(),
                option.getSortOrder(),
                productIds
        );
    }
}
