package tea4life.product_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.response.PopularProductCardResponse;
import tea4life.product_service.dto.response.ProductCategoryResponse;
import tea4life.product_service.dto.response.ProductDetailResponse;
import tea4life.product_service.dto.response.ProductSummaryResponse;
import tea4life.product_service.service.ProductCategoryAdminService;
import tea4life.product_service.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/public/products")
public class ProductController {

    ProductService productService;
    ProductCategoryAdminService productCategoryAdminService;

    @GetMapping
    public ApiResponse<PageResponse<ProductSummaryResponse>> findProducts(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice
    ) {
        return new ApiResponse<>(productService.findProducts(pageable, keyword, categoryId, minPrice, maxPrice));
    }

    @GetMapping("/popular")
    public ApiResponse<List<PopularProductCardResponse>> getPopularProducts(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return new ApiResponse<>(productService.getPopularProducts(limit));
    }

    @GetMapping("/categories")
    public ApiResponse<List<ProductCategoryResponse>> findCategories() {
        return new ApiResponse<>(productCategoryAdminService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> findProductById(
            @PathVariable("id") Long id
    ) {
        return new ApiResponse<>(productService.findProductById(id));
    }
}
