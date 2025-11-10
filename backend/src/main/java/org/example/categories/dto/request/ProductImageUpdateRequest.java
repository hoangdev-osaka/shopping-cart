package org.example.categories.dto.request;

public record ProductImageUpdateRequest(
        String imageUrl,
        String altText,
        Boolean isMain,
        Integer width,
        Integer heigh,
        String mimeType,
        Long size_bytes,
        String checksum
) {
}
