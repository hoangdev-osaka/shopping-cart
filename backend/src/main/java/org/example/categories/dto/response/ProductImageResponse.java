package org.example.categories.dto.response;

import org.example.common.enums.status.ProductImageStatus;

public record ProductImageResponse(
        Long id,
        String imageUrl,
        String altText,
        boolean main,
        Integer width,
        Integer height,
        String mimeType,
        Long sizeBytes,
        ProductImageStatus status,
        Integer sortOrder
) {
}
