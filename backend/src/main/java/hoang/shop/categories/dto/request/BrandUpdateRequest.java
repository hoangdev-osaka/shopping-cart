package hoang.shop.categories.dto.request;

import hoang.shop.common.enums.status.BrandStatus;
import jakarta.validation.constraints.NotBlank;

public record BrandUpdateRequest(
        @NotBlank
        String name,
        @NotBlank
        String slug,
        String description,
        String logoUrl,
        BrandStatus status
) {
}
