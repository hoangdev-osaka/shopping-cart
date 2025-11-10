package org.example.categories.dto.response;

import org.example.categories.model.Product;
import org.example.common.enums.status.TagStatus;

import java.util.Set;

public record TagResponse(
        Long id,
        String name,
        String slug,
        String description,
        TagStatus status,
        Set<ProductResponse> products
) {
}
