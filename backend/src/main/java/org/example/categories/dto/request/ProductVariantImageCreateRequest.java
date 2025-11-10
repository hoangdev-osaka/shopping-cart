package org.example.categories.dto.request;


import org.example.categories.model.ProductVariant;

public record ProductVariantImageCreateRequest(
        Long productVariantId,
        String imageUrl,
        String  altText,
        boolean isMain,
        Integer width,
        Integer height,
        String mimeType,
        Long size_bytes,
        String checksum

){
}
