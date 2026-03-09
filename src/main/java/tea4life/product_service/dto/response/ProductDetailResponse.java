package tea4life.product_service.dto.response;

import java.util.List;

public record ProductDetailResponse(
        String id,
        ProductCategoryResponse productCategory,
        String name,
        String description,
        Double basePrice,
        String imageUrl,
        List<ProductOptionResponse> productOptions
) {
}
