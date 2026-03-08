package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductRequest(
        @NotBlank(message = "productCategoryId khong duoc de trong")
        String productCategoryId,
        @NotBlank(message = "Ten san pham khong duoc de trong")
        String name,
        String description,
        @NotNull(message = "basePrice khong duoc de trong")
        Double basePrice,
        String imageKey,
        List<String> productOptionIds
) {
}
