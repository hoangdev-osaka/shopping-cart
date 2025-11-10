package org.example.categories.dto.request;

import org.example.common.enums.status.ProductStatus;

import java.math.BigDecimal;

public record ProductFilterRequest(
        Long categoryId,
        Long brandId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        ProductStatus status
) {
}
