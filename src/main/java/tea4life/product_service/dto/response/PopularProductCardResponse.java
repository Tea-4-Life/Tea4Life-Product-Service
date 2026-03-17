package tea4life.product_service.dto.response;

public record PopularProductCardResponse(
        String id,
        String name,
        Double basePrice,
        String imageUrl,
        String productCategoryName,
        ProductPopularityResponse popularity
) {
}
