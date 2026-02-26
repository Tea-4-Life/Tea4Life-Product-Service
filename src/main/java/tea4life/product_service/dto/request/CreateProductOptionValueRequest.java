package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductOptionValueRequest(
        @NotBlank(message = "Tên giá trị option không được để trống")
        String valueName,
        @NotNull(message = "extraPrice không được để trống")
        Double extraPrice,
        @NotNull(message = "sortOrder không được để trống")
        Integer sortOrder
) {
}
