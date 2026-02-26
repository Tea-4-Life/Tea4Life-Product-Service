package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductOptionRequest(
        @NotBlank(message = "Tên option không được để trống")
        String name,
        boolean isRequired,
        boolean isMultiSelect,
        @NotNull(message = "sortOrder không được để trống")
        Integer sortOrder,
        List<Long> productIds
) {
}
