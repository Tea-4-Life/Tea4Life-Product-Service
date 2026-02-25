package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Admin 2/25/2026
 *
 **/
public record CreateProductCategoryRequest(
        @NotBlank(message = "Tên danh mục không được để trống")
        String name,
        String description,
        String iconKey
) {
}
