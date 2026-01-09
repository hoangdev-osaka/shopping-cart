package hoang.shop.categories.dto.request;

import hoang.shop.common.enums.status.ProductVariantStatus;

public record ProductVariantSearchCondition(
        Long productId,
        Long colorId,
        String size,
        Integer minStock,
        Integer maxStock,
        String keyword,
        ProductVariantStatus status

) {
}
