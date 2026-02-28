package tea4life.product_service.dto.response;

import java.util.List;

public record ProductOptionResponse(
        String id,
        String name,
        boolean isRequired,
        boolean isMultiSelect,
        Integer sortOrder,
        List<ProductOptionValueResponse> productOptionValues
) {
}
