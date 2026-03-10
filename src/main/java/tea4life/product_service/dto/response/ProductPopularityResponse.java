package tea4life.product_service.dto.response;

import java.time.Instant;

public record ProductPopularityResponse(
        String productId,
        Long viewCount,
        Long clickCount,
        Long orderCount,
        Double totalScore,
        Instant lastUpdated
) {

    public static ProductPopularityResponse empty(Long productId) {
        return new ProductPopularityResponse(
                productId == null ? null : productId.toString(),
                0L,
                0L,
                0L,
                0.0,
                null
        );
    }
}
