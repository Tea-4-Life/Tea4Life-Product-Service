package tea4life.product_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import tea4life.product_service.dto.base.ApiResponse;
import tea4life.product_service.dto.response.ProductPopularityResponse;

import java.util.List;

@FeignClient(name = "TEA4LIFE-RECOMMENDATION-SERVICE", url = "${service.url.recommendation}")
public interface RecommendationClient {

    @GetMapping("/internal/recommendations/products/{productId}/popularity")
    ApiResponse<ProductPopularityResponse> getProductPopularity(@PathVariable("productId") Long productId);

    @GetMapping("/internal/recommendations/popularity")
    ApiResponse<List<ProductPopularityResponse>> getProductPopularities(
            @RequestParam("productIds") List<Long> productIds
    );
}
