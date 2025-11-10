package org.example.categories.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.math.BigDecimal;

public record ProductVariantResponse(
        Long id,
        String name,
        String color,
        String size,
        BigDecimal price,
        BigDecimal compareAtPrice,
        Integer stockQuantity

) {
}
