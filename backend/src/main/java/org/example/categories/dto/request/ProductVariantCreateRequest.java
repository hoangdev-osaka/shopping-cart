package org.example.categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.example.categories.model.Product;

import java.math.BigDecimal;

public record ProductVariantCreateRequest(
        String name,
        String  color,
        String size,
        BigDecimal price,
        BigDecimal compareAtPrice,
        Integer stockQuantity
) {
}
