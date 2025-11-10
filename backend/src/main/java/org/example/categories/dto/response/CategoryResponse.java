package org.example.categories.dto.response;

import org.example.common.enums.status.CategoryStatus;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        CategoryParentResponse parent,
        String imageUrl,
        CategoryStatus status
) {
}
