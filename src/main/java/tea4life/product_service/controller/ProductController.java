package tea4life.product_service.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.base.PageResponse;
import tea4life.product_service.dto.response.ProductResponse;
import tea4life.product_service.dto.response.ProductSummaryResponse;
import tea4life.product_service.service.ProductService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    @GetMapping()
    public ApiResponse<PageResponse<ProductSummaryResponse>> findProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return new ApiResponse<>(productService.findProducts(page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> findProductById(
            @PathVariable("id") Long id
    ) {
        return new ApiResponse<>(productService.findProductById(id));
    }
}
