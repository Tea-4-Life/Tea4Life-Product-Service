package tea4life.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductOptionValueRequest(
        String productOptionId,
        @NotBlank(message = "Tˆn gi  tr? option kh“ng du?c d? tr?ng")
        String valueName,
        @NotNull(message = "extraPrice kh“ng du?c d? tr?ng")
        Double extraPrice,
        @NotNull(message = "sortOrder kh“ng du?c d? tr?ng")
        Integer sortOrder
) {
}
