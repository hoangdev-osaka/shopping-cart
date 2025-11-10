package org.example.categories.dto.request;

import org.example.common.enums.status.TagStatus;

public record TagCreateRequest(
        String name,
        String slug,
        String description
) {
}
