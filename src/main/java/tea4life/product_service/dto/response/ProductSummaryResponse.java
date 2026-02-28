package tea4life.product_service.dto.response;

public record ProductSummaryResponse(
        String id,
        String name,
        Double basePrice,
        String imageUrl,
        String productCategoryName
) {
}
