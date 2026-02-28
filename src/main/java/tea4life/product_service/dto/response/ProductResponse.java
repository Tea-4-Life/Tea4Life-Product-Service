package tea4life.product_service.dto.response;

import java.util.List;

public record ProductResponse(
        String id,
        String productCategoryId,
        String productCategoryName,
        String name,
        String description,
        Double basePrice,
        String imageUrl,
        List<String> productOptionIds
) {
}
