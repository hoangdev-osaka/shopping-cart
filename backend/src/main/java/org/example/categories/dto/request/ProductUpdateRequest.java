package org.example.categories.dto.request;

import org.example.common.enums.status.ProductStatus;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        String name,
        String description,
        String slug,
        BigDecimal price,
        BigDecimal discountPrice,
        Integer stockQuantity,
        ProductStatus status

) {
}
