package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductOptionValueRequest(
        String productOptionId,
        @NotBlank(message = "Tên tùy chọn giá trị sản phẩm không được để trống")
        String valueName,
        @NotNull(message = "Giá thêm không được để trống")
        Double extraPrice,
        @NotNull(message = "Thứ tự không được để trống")
        Integer sortOrder,
        String imageKey
) {
}
