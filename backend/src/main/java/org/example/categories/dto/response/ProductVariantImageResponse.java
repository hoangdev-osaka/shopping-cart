package org.example.categories.dto.response;

public record ProductVariantImageResponse(
        Long id,
        String imageUrl,
        String altText,
        boolean main,
        Integer width,
        Integer height,
        String mimeType,
        Long sizeBytes,
        Integer sortOrder
) {
}
