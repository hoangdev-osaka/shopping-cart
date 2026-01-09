package hoang.shop.categories.dto.response;

import hoang.shop.common.enums.status.ProductColorStatus;

import java.time.Instant;

public record AdminColorResponse(

        Long id,
        Long productId,
        String name,
        String hex,
        ProductColorStatus status,
        boolean main,
        Instant createdAt,
        Long createdBy,
        Instant updatedAt,
        Long updatedBy,
        Instant deletedAt,
        Long deletedBy
) {
}
