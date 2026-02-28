package tea4life.product_service.controller.admin;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.*;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.request.CreateProductOptionRequest;
import tea4life.product_service.dto.request.CreateProductOptionValueRequest;
import tea4life.product_service.dto.response.ProductOptionResponse;
import tea4life.product_service.dto.response.ProductOptionValueResponse;
import tea4life.product_service.service.ProductOptionAdminService;
import tea4life.product_service.service.ProductOptionValueAdminService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/product-options")
public class ProductOptionAdminController {

    ProductOptionAdminService productOptionAdminService;
    ProductOptionValueAdminService productOptionValueAdminService;

    @PostMapping()
    public ApiResponse<ProductOptionResponse> createOption(
            @RequestBody @Valid CreateProductOptionRequest request
    ) {
        return new ApiResponse<>(productOptionAdminService.createOption(request));
    }

    @GetMapping()
    public ApiResponse<List<ProductOptionResponse>> findAllOptions() {
        return new ApiResponse<>(productOptionAdminService.findAllOptions());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductOptionResponse> findOptionById(
            @PathVariable("id") Long id
    ) {
        return new ApiResponse<>(productOptionAdminService.findOptionById(id));
    }

    @PostMapping("/{id}")
    public ApiResponse<ProductOptionResponse> updateOption(
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateProductOptionRequest request
    ) {
        return new ApiResponse<>(productOptionAdminService.updateOption(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<@NonNull Void> deleteOption(
            @PathVariable("id") Long id
    ) {
        productOptionAdminService.deleteOption(id);
        return new ApiResponse<>((Void) null);
    }

    @PostMapping("/{productOptionId}/values")
    public ApiResponse<ProductOptionValueResponse> createValue(
            @PathVariable("productOptionId") Long productOptionId,
            @RequestBody @Valid CreateProductOptionValueRequest request
    ) {
        return new ApiResponse<>(productOptionValueAdminService.createValue(productOptionId, request));
    }

    @GetMapping("/{productOptionId}/values")
    public ApiResponse<PageResponse<ProductOptionValueResponse>> findAllValues(
            @PathVariable("productOptionId") Long productOptionId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return new ApiResponse<>(productOptionValueAdminService.findAllValues(productOptionId, page, size));
    }

    @GetMapping("/{productOptionId}/values/{id}")
    public ApiResponse<ProductOptionValueResponse> findValueById(
            @PathVariable("productOptionId") Long productOptionId,
            @PathVariable("id") Long id
    ) {
        return new ApiResponse<>(productOptionValueAdminService.findValueById(productOptionId, id));
    }

    @PostMapping("/{productOptionId}/values/{id}")
    public ApiResponse<ProductOptionValueResponse> updateValue(
            @PathVariable("productOptionId") Long productOptionId,
            @PathVariable("id") Long id,
            @RequestBody @Valid CreateProductOptionValueRequest request
    ) {
        return new ApiResponse<>(productOptionValueAdminService.updateValue(productOptionId, id, request));
    }

    @DeleteMapping("/{productOptionId}/values/{id}")
    public ApiResponse<@NonNull Void> deleteValue(
            @PathVariable("productOptionId") Long productOptionId,
            @PathVariable("id") Long id
    ) {
        productOptionValueAdminService.deleteValue(productOptionId, id);
        return new ApiResponse<>((Void) null);
    }
}
