package org.example.categories.dto.response;

import org.example.categories.model.Brand;
import org.example.categories.model.Tag;
import org.example.common.enums.status.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String slug,
        BigDecimal price,
        BigDecimal discountPrice,
        Integer stockQuantity,
        ProductStatus status

) {
}
