package tea4life.product_service.dto.response;

import lombok.Builder;

@Builder
public record ProductOptionValueResponse(
        String id,
        String productOptionId,
        String valueName,
        Double extraPrice,
        Integer sortOrder
) {
}
