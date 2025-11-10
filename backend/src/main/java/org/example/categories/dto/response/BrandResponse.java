package org.example.categories.dto.response;

import org.example.common.enums.status.BrandStatus;

public record BrandResponse(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl,
        BrandStatus status
) {
}
