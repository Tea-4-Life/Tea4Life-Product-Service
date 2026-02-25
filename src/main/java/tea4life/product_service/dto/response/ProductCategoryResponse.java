package tea4life.product_service.dto.response;

import lombok.Builder;

/**
 * Admin 2/25/2026
 *
 **/
@Builder
public record ProductCategoryResponse(
        String id,
        String name,
        String description,
        String iconUrl
) {
}
