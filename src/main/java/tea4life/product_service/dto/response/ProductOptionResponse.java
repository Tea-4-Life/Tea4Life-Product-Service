package tea4life.product_service.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductOptionResponse(
        String id,
        String name,
        boolean isRequired,
        boolean isMultiSelect,
        Integer sortOrder,
        List<String> productIds
) {
}
